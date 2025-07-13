package renovation.batch.service.base;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import renovation.batch.service.configs.PostgresSQLContainerConfigs;
import renovation.common.util.MapperUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(
        classes = PostgresSQLContainerConfigs.class
)
public abstract class DatabaseFillTestBase {

    @PostConstruct
    void clearDatabaseBeforeTests() {
        clearDatabase();
    }

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeEach
    protected void fillDatabase() {
        jdbcTemplate.update(
                MapperUtil.readFileContentFromProjectRoot(
                        "src/test/resources/fill_data.sql"
                )
        );
    }

    @AfterEach
    protected void clearDatabase() {
        jdbcTemplate.update("delete from \"person\"");
    }

    protected void entityRowsCountEquals(long expectedCount, String entity) {
        final String COUNT_WORD = "count";

        assertEquals(
                expectedCount,
                jdbcTemplate.queryForList("select count(*) from " + entity).getFirst().get(COUNT_WORD)
        );
    }
}
