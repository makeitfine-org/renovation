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
import org.springframework.test.context.ContextConfiguration;
import renovation.neo4j.service.web.Route;
import renovation.neo4j.service.web.controller.base.Neo4jTestcontainers;
import renovation.neo4j.service.web.controller.base.RestTestInit;

import static renovation.common.util.JsonUtil.simplify;
import static renovation.common.util.MapperUtil.jsonFileContentFromSrcTestResources;

@Tag("componentTest")
@ContextConfiguration(classes = Neo4jTestcontainers.class)
class AuthorControllerTest extends RestTestInit {

    public AuthorControllerTest() {
        super(Route.AUTHOR);
    }

    @Test
    void when_Save_expect_Success() {

        request.body("""
                        {
                            "id"          : 12,
                            "firstName"   : "Igor",
                            "lastName"    : "Fainiv",
                            "middleName"  : "Kindratovych",
                            "birthDate"   : "1971-11-25"
                        }
                        """.trim())
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_CREATED);
        request.body("""
                        {
                            "firstName"   : "Petro",
                            "lastName"    : "Lomiv",
                            "middleName"  : "Kindratovych",
                            "birthDate"   : "1979-03-02"
                        }
                        """.trim())
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        // Then
        request.get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                jsonFileContentFromSrcTestResources(
                                        "AuthorControllerTest.when_Save_expect_Success.json"
                                )
                        )
                ));
    }
}
