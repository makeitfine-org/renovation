/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.reacty.service.web.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import renovation.reacty.service.web.Route;

@Tag("componentTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReactiveControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testModule() {
        webTestClient
                .get().uri(Route.STARTUP + "/module") // GET method and URI
                .accept(MediaType.TEXT_PLAIN) //setting ACCEPT-Content
                .exchange() //gives access to response
                .expectStatus().isOk() //checking if response is OK
                .expectBody(String.class).isEqualTo("Hi, it's \"Reacty-service\" module"); // checking for response type and message
    }
}
