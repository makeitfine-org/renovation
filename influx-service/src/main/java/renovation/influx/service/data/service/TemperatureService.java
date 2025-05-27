/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.influx.service.data.service;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import renovation.influx.service.data.entity.TemperatureMeasurement;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TemperatureService {

    private final InfluxDB influxDB;

    private final String database;

    public TemperatureService(
            InfluxDB influxDB,
            @Value("${influx.database}") String database
    ) {
        this.influxDB = influxDB;
        this.database = database;
    }

    public void writeTemperature(String location, Double value) {
        Point point = Point.measurement("temperature")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("location", location)
                .addField("value", value)
                .build();

        influxDB.write(point);
    }

    public List<TemperatureMeasurement> readTemperature(String location) {
        String q = String.format("SELECT * FROM temperature WHERE location = '%s'", location);
        Query query = new Query(q, database);
        return new InfluxDBResultMapper()
                .toPOJO(
                        influxDB.query(query),
                        TemperatureMeasurement.class
                );
    }
}
