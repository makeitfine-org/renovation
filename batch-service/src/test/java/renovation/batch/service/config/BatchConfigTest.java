/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.batch.service.config;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import renovation.batch.service.base.PostgresSQLContainerConfigs;
import renovation.batch.service.config.model.UserModel;
import renovation.batch.service.data.repository.BatchRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integrationTest")
@SpringBootTest
@ContextConfiguration(classes = PostgresSQLContainerConfigs.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AllArgsConstructor
class BatchConfigTest {

    @Autowired
    private final BatchRepository batchRepository;

    @Test
    void testBatchJobRunsSuccessfully() throws Exception {
        assertEquals(
                Set.of(new UserModel("John", "Doe"), new UserModel("Jane", "Smith")),
                batchRepository.getResults()
        );
    }
}
