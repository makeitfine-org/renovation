package renovation.batch.service.configs;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class PostgresSQLContainerConfigs {
    private static final String POSTGRES_VERSION = "postgres:17.1-alpine";

    @Bean
    @ServiceConnection(name = "postgres")
    public PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer(POSTGRES_VERSION);
    }
}
