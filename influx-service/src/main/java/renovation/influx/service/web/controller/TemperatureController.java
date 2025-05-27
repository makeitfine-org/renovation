/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.influx.service.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import renovation.influx.service.data.entity.TemperatureMeasurement;
import renovation.influx.service.data.service.TemperatureService;
import renovation.influx.service.web.Route;

import java.util.List;

@RestController
@RequestMapping(Route.TEMPERATURE)
@RequiredArgsConstructor
public class TemperatureController {

    private final TemperatureService temperatureService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void write(@RequestParam String location, @RequestParam double value) {
        temperatureService.writeTemperature(location, value);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TemperatureMeasurement> read(@RequestParam String location) {
        return temperatureService.readTemperature(location);
    }
}
