/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import renovation.event.service.web.Route;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(value = Route.STARTUP, produces = MediaType.APPLICATION_JSON_VALUE)
public class StartupController {

    private final String applicationName;
    private final String applicationDescription;

    public StartupController(
            @Value("${spring.application.name}") String applicationName,
            @Value("${info.app.description}") String applicationDescription
    ) {
        this.applicationName = applicationName;
        this.applicationDescription = applicationDescription;
    }

    @GetMapping("/about")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> about() {
        return Collections.unmodifiableMap(
                new LinkedHashMap<>() {{
                    put("name", applicationName);
                    put("description", applicationDescription);
                }}
        );
    }

    @GetMapping("/module")
    @ResponseStatus(HttpStatus.OK)
    public String index() {
        return "Hi, it's \"" + StringUtils.capitalize(applicationName) + "\" module";
    }
}
