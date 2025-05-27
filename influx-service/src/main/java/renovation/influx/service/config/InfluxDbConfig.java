/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.influx.service.config;

import lombok.RequiredArgsConstructor;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InfluxDbConfig {

    @Value("${influx.url}")
    private String url;

    @Value("${influx.database}")
    private String database;

    @Value("${influx.username}")
    private String username;

    @Value("${influx.password}")
    private String password;

    @Bean
    public InfluxDB influxDB() {
        InfluxDB influxDB = InfluxDBFactory.connect(url, username, password);
        influxDB.setDatabase(database);
        influxDB.enableBatch(); // Optional: enables batch mode for writes
        return influxDB;
    }
}
