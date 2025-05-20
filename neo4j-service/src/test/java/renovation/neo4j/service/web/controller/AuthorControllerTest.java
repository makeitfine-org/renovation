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
import renovation.neo4j.service.web.controller.base.Neo4jRestTestInit;

import static renovation.common.util.JsonUtil.simplify;
import static renovation.common.util.MapperUtil.jsonFileContentFromSrcTestResources;

@Tag("componentTest")
class AuthorControllerTest extends Neo4jRestTestInit {

    public AuthorControllerTest() {
        super(Route.AUTHOR);
    }

    @Test
    void when_GetAll_expect_Success() {
        getRequest().get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                jsonFileContentFromSrcTestResources(
                                        "AuthorControllerTest.when_GetAll_expect_Success.json"
                                )
                        )
                ));
    }

    @Test
    void when_GetById_expect_Success() {
        getRequest().get("/5")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                """
                                        {
                                            "id": 5,
                                            "firstName": "Petro",
                                            "lastName": "Lomiv",
                                            "middleName": "Kindratovych",
                                            "birthDate": "1979-03-02"
                                          }
                                        """.trim()
                        )));
    }

    @Test
    void when_Create_expect_Success() {
        getRequest().body("""
                        {
                            "id": 2,
                            "firstName"   : "Igor",
                            "lastName"    : "Fainiv",
                            "middleName"  : "Matov",
                            "birthDate"   : "1975-11-25"
                        }
                        """.trim())
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body(CoreMatchers.equalTo(
                        simplify(
                                """
                                        {
                                            "id"          : 2,
                                            "firstName"   : "Igor",
                                            "lastName"    : "Fainiv",
                                            "middleName"  : "Matov",
                                            "birthDate"   : "1975-11-25"
                                        }
                                        """.trim()
                        )
                ));

        // Then
        getRequest().get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                jsonFileContentFromSrcTestResources(
                                        "AuthorControllerTest.when_Create_expect_Success.json"
                                )
                        )
                ));
    }

    @Test
    void when_Delete_expect_Success() {
        getRequest().delete("/1")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        // Then
        getRequest().get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                """
                                        [
                                           {
                                                "id": 5,
                                                "firstName": "Petro",
                                                "lastName": "Lomiv",
                                                "middleName": "Kindratovych",
                                                "birthDate": "1979-03-02"
                                            }
                                        ]
                                        """.trim()
                        )
                ));
    }
}
