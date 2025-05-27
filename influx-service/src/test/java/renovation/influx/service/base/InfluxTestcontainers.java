/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.influx.service.base;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.InfluxDBContainer;


@TestConfiguration(proxyBeanMethods = false)
public class InfluxTestcontainers {
    public static final String INFLUX_IMAGE = "influxdb:1.11";

    @Bean
    @ServiceConnection(name = "influx")
    public InfluxDBContainer influxContainer() {
        return new InfluxDBContainer(INFLUX_IMAGE);
    }
}
