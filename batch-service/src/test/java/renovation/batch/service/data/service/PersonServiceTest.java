/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.batch.service.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;
import renovation.batch.service.base.DatabaseFillTestBase;
import renovation.common.util.JsonUtil;
import renovation.common.util.MapperUtil;

@Tag("integrationTest")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AllArgsConstructor
public class PersonServiceTest extends DatabaseFillTestBase {

    private final PersonService personService;

    @Test
    void fillPersonTable_Success() throws JsonProcessingException {
        entityRowsCountEquals(3, "person");

        var findAll = personService.findAll(Sort.by("id"));
        var bodyActual = MapperUtil.OBJECT_MAPPER.writeValueAsString(findAll);

        var bodyExpected = MapperUtil.jsonFileContentFromSrcTestResources(
                "FillDatabaseTest.fillPersonTable_Success.json"
        );

        Assertions.assertEquals(
                JsonUtil.simplify(bodyExpected),
                bodyActual
        );
    }
}
