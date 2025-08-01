/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.reacty.service.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import renovation.reacty.service.web.Route;

import java.time.Duration;

@Configuration
public class ModuleRouter {

    @Bean
    public RouterFunction<ServerResponse> routeHelloWorld(ModuleHandler moduleHandler) {
        return RouterFunctions.route(RequestPredicates.GET(Route.STARTUP + "/module")
                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), moduleHandler::module);
    }
}

@Component
class ModuleHandler {
    private final String applicationName;

    ModuleHandler(@Value("${spring.application.name}") String applicationName) {
        this.applicationName = applicationName;
    }

    public Mono<ServerResponse> module(ServerRequest request) {
        return Mono
                .delay(Duration.ofSeconds(2)) // 2-second async delay
                .flatMap(
                        tick -> ServerResponse.ok()
                                .contentType(MediaType.TEXT_PLAIN)
                                .body(BodyInserters.fromValue(
                                        "Hi, it's \"" + StringUtils.capitalize(applicationName) + "\" module")
                                )
                );
    }
}
