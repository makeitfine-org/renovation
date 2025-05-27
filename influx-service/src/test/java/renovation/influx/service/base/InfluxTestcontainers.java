/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.influx.service.base;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.InfluxDBContainer;
import org.testcontainers.utility.DockerImageName;


@TestConfiguration(proxyBeanMethods = false)
public class InfluxTestcontainers {
    public static final DockerImageName INFLUX_IMAGE = DockerImageName.parse("influxdb:1.11");

    @Bean
    @ServiceConnection(name = "influx")
    public InfluxDBContainer influxContainer() {
        return new InfluxDBContainer(INFLUX_IMAGE);
    }

    @Bean
    DynamicPropertyRegistrar kafkaProperties(InfluxDBContainer<?> influxDBContainer) {
        return registry -> {
            registry.add("influx.url", influxDBContainer::getUrl);
            registry.add("influx.database", influxDBContainer::getDatabase);
            registry.add("influx.username", influxDBContainer::getUsername);
            registry.add("influx.password", influxDBContainer::getPassword);
        };
    }
}
