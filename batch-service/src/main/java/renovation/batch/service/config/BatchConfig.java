/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.batch.service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;
import renovation.batch.service.config.model.UserModel;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private static final String DELIMITER = ";";
    private static final String FILE_PATH = "batch/users.csv";
    private static final Integer CHUNK_SIZE = 10;

    @Bean
    public FlatFileItemReader<UserModel> reader() {
        return new FlatFileItemReaderBuilder<UserModel>()
                .name("userItemReader")
                .resource(new ClassPathResource(FILE_PATH))
                .delimited()
                .delimiter(DELIMITER)
                .names("firstName", "lastName")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(UserModel.class);
                }})
                .recordSeparatorPolicy(new DefaultRecordSeparatorPolicy())
                .build();
    }

//    @Bean
//    public ItemReader<UserModel> reader() {
//        return new ListItemReader<>(new ArrayList<>() {{
//            add(new UserModel("John", "Doe"));
//            add(new UserModel("Tom", "AbDoe"));
//        }});
//    }

    @Bean
    public ItemProcessor<UserModel, UserModel> processor() {
        return user -> {
            return new UserModel(
                    StringUtils.capitalize(user.firstName().toLowerCase()),
                    StringUtils.capitalize(user.lastName().toLowerCase())
            );
        };
    }

    @Bean
    public ItemWriter<UserModel> writer() {
        return items -> {
            for (UserModel user : items) {
                System.out.println(">> Processed user: " + user);
            }
        };
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<UserModel, UserModel>chunk(CHUNK_SIZE, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .start(step1)
                .build();
    }
}
