/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.batch.service.config;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import renovation.batch.service.base.PostgresSQLContainerConfigs;
import renovation.batch.service.config.model.UserModel;
import renovation.batch.service.data.repository.BatchRepository;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integrationTest")
@SpringBatchTest
@SpringBootTest(properties = "spring.batch.job.enabled=false")
@ContextConfiguration(classes = PostgresSQLContainerConfigs.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AllArgsConstructor
class BatchConfigUnitTest {

    private final BatchRepository batchRepository;
    private JobLauncherTestUtils jobLauncherTestUtils;
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    private Job importUserJob;
    private JobExplorer jobExplorer;

    @BeforeEach
    void setUp() {
        jobLauncherTestUtils.setJob(importUserJob);
    }

    @AfterEach
    void tearDown() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testBatchJobActivatedSuccessfully() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

        assertThat(jobExplorer.getJobInstances(importUserJob.getName(), 0, 1)).isNotEmpty();
        assertEquals(
                Set.of(new UserModel("John", "Doe"), new UserModel("Jane", "Smith")),
                batchRepository.getResults()
        );
    }

    @Test
    void testBatchJobNotActivatedSuccessfully() throws Exception {
        assertThat(jobExplorer.getJobInstances(importUserJob.getName(), 0, 1)).isEmpty();
        assertThat(batchRepository.getResults()).isEmpty();
    }
}
