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
import org.springframework.test.annotation.DirtiesContext;
import renovation.neo4j.service.web.Route;
import renovation.neo4j.service.web.controller.base.Neo4jRestTestInit;

import static renovation.common.util.JsonUtil.simplify;
import static renovation.common.util.MapperUtil.jsonFileContentFromSrcTestResources;

@Tag("componentTest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) //todo: remove after rem. author autogen.
class BookControllerTest extends Neo4jRestTestInit {

    public BookControllerTest() {
        super(Route.BOOK);
    }

    @Test
    void when_FindAllByYear_expect_Success() {
        getRequest().get("/find/by/year/2022")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                jsonFileContentFromSrcTestResources(
                                        "BookControllerTest.when_FindAllByYear_expect_Success.json"
                                )
                        )
                ));
    }

    @Test
    void when_FindOneByTitle_expect_Success() {
        getRequest().get("/find/by/title/{title}", "Spring in Action")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                """
                                         {
                                             "id": 1,
                                             "title": "Spring in Action",
                                             "year": 2021,
                                             "authorId": 0
                                           }
                                        """.trim()
                        )
                ));
    }

    @Test
    void when_FindBooksAfterYear_expect_Success() {
        getRequest().get("/find/after/year/2022")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                jsonFileContentFromSrcTestResources(
                                        "BookControllerTest.when_FindBooksAfterYear_expect_Success.json"
                                )
                        )
                ));
    }
}
