/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.common.web;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

/**
 * It's used only with spring boot test.
 */
public abstract class RestTestInit {

    @LocalServerPort
    private Integer port;

    private String basePath;

    private RequestSpecification request;

    public RestTestInit(String basePath) {
        this.basePath = basePath;
    }

    public RestTestInit() {
    }

    @PostConstruct
    protected void init() {
        RestAssured.port = port;

        if (basePath != null)
            request = given()
                    .header("Content-Type", "application/json")
                    .basePath(basePath);
    }

    public String getBasePath() {
        return basePath;
    }

    public Integer getPort() {
        return port;
    }

    public RequestSpecification getRequest() {
        return request;
    }

    /**
     * Prevent caching.
     *
     * @return
     */
    public RequestSpecification getRequestNew() {
        return given()
                .port(getPort())
                .header("Content-Type", "application/json")
                .basePath(basePath);
    }
}
