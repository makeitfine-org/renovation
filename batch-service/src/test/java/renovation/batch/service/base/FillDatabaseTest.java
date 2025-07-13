/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.batch.service.base;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;
import renovation.batch.service.data.repository.PersonRepository;

@Tag("integrationTest")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AllArgsConstructor
public class FillDatabaseTest extends DatabaseFillTestBase {

    private final PersonRepository personRepository;

    @Test
    void fillPersonTable_Success() {
        entityRowsCountEquals(3, "person");
    }
}
