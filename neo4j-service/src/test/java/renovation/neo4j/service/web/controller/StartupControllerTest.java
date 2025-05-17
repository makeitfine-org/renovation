/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.neo4j.service.web.controller;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import renovation.neo4j.service.web.Route;
import renovation.neo4j.service.web.controller.base.RestTestInit;

import static renovation.neo4j.service.TestUtil.simplify;

@Tag("componentTest")
class StartupControllerTest extends RestTestInit {

    public StartupControllerTest() {
        super(Route.STARTUP);
    }

    @Test
    void about() {
        request
                .when()
                .get("/about")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                """
                                        {
                                            "name"        : "neo4j-service",
                                            "description" : "Neo4j service module"
                                        }
                                        """)
                ));
    }

    @Test
    void index() {
        request
                .when()
                .get("/module")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo("Hi, it's \"Neo4j-service\" module"));
    }
}
