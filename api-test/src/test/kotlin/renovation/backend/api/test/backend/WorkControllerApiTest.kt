/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test.backend

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.jupiter.api.Tag
import renovation.backend.api.test.ApiTest
import renovation.backend.api.test.helper.FileHelper
import renovation.backend.api.test.helper.SecurityHelper
import renovation.common.util.Json.OBJECT_MAPPER
import renovation.common.util.Json.rowJson

@Tag("e2eTest")
internal class WorkControllerApiTest : ApiTest {

    @Test
    fun `find all`() {
        given()
            .When {
                get(BackendServerRoute.API_WORK.route)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)

                val expected = FileHelper.WORK_CONTROLLER_GET_ALL_RESPONSE.fileContent
                body(equalTo(rowJson(expected)))
            }
    }

    @Test
    fun `find list by title like`() {
        given()
            .queryParam("title", "ti")
            .When {
                get(BackendServerRoute.API_WORK.route)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)

                val expected = FileHelper.WORK_CONTROLLER_GET_TITLE_LIKE_RESPONSE.fileContent
                body(equalTo(rowJson(expected)))
            }
    }

    @Test
    fun `find list by title like which not existence (not equals)`() {
        given()
            .queryParam("title", "ti${UUID.randomUUID()}")
            .When {
                get(BackendServerRoute.API_WORK.route)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)

                val expected = FileHelper.WORK_CONTROLLER_GET_TITLE_LIKE_RESPONSE.fileContent
                body(not(equalTo(rowJson(expected))))
                body(equalTo("[]"))
            }
    }

    @Test
    fun `find by id`() {
        given()
            .pathParam("id", "55555555-a845-45d7-aea9-ab624172d1c1")
            .When {
                get(BackendServerRoute.API_WORK_ID.route)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)

                val expected = FileHelper.WORK_CONTROLLER_GET_BY_ID_RESPONSE.fileContent
                body(equalTo(rowJson(expected)))
            }
    }

    @Test
    fun `find by wrong format id (failed)`() {
        given()
            .pathParam("id", "5a")
            .When {
                get(BackendServerRoute.API_WORK_ID.route)
            }
            .Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)

                body(equalTo("Internal server error (500)"))
            }
    }

    @Test
    fun `find by not existence id (failed)`() {
        given()
            .pathParam("id", "111cc111-a845-45d7-aea9-ab624172d1c1")
            .When {
                get(BackendServerRoute.API_WORK_ID.route)
            }
            .Then {
                statusCode(HttpStatus.SC_NOT_FOUND)
            }
    }

    @Test
    fun update() {
        val id = "44444444-a845-45d7-aea9-ab624172d1c1"
        val titleUpdated = "title update 23713311"
        given()
            .When {
                body(
                    """
                {
                   "title":"$titleUpdated",
                   "description":"desc update",
                   "price":773.31,
                   "payDate":"2020-11-18"
                }
                    """.trimIndent()
                )
                    .pathParam("id", id)
                    .patch(BackendServerRoute.API_WORK_ID.route)
            }
            .Then {
                statusCode(HttpStatus.SC_NO_CONTENT)

                given()
                    .When {
                        queryParam("title", titleUpdated)
                            .get(BackendServerRoute.API_WORK.route)
                    }
                    .Then {
                        statusCode(HttpStatus.SC_OK)

                        body(
                            equalTo(
                                rowJson(
                                    """
                            [
                                {
                                    "id": "$id",
                                    "title": "$titleUpdated",
                                    "description": "desc update",
                                    "endDate": "2021-12-01",
                                    "price": 773.31,
                                    "payDate": "2020-11-18"
                                }
                            ]
                                    """.trimIndent()
                                )
                            )
                        )
                    }
            }

        // Undo update
        given().body(
            """
                {
                    "title": "title sticker",
                    "description": "",
                    "price": 33000.0,
                    "payDate": "2021-12-05"
                }
            """.trimIndent()
        )
            .pathParam("id", id)
            .patch(BackendServerRoute.API_WORK_ID.route)
    }

    @Test
    fun `update with not existence id (failed)`() {
        val id = "1111dcba-a845-45d7-aea9-ab624172d1c1"
        given()
            .When {
                body(
                    """
                {
                   "title":"title update"
                }
                    """.trimIndent()
                )
                    .pathParam("id", id)
                    .patch(BackendServerRoute.API_WORK_ID.route)
            }
            .Then {
                statusCode(HttpStatus.SC_NOT_FOUND)
            }
    }

    @Test
    fun `update with invalid data (failed)`() {
        val id = "55555555-a845-45d7-aea9-ab624172d1c1"
        given()
            .When {
                body(
                    """
                {
                   "title":"title update",
                   "price":773.31a
                }
                    """.trimIndent()
                )
                    .pathParam("id", id)
                    .patch(BackendServerRoute.API_WORK_ID.route)
            }
            .Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            }
    }

    @Test
    fun create() {
        val titleSaved = "title saved 2352321152"
        given()
            .When {
                body(
                    """
                {
                   "title":"$titleSaved",
                   "description":"desc save",
                   "price":773.31,
                   "payDate":"2020-11-28"
                }
                    """.trimIndent()
                ).post(BackendServerRoute.API_WORK.route)
            }
            .Then {
                statusCode(HttpStatus.SC_CREATED)

                given()
                    .When {
                        queryParam("title", titleSaved)
                            .get(BackendServerRoute.API_WORK.route)
                    }
                    .Then {
                        statusCode(HttpStatus.SC_OK)

                        body(containsString(rowJson("\"title\": \"$titleSaved\"")))
                        body(containsString(rowJson("\"description\": \"desc save\"")))
                        body(containsString(rowJson("\"price\": 773.31")))
                        body(containsString(rowJson("\"payDate\": \"2020-11-28\"")))
                    }
            }

        // Undo create
        val getSavedBody = given()
            .queryParam("title", titleSaved)
            .get(BackendServerRoute.API_WORK.route)
            .body.asString()

        val savedId = OBJECT_MAPPER.readTree(getSavedBody)[0]["id"].asText()

        given()
            .pathParam("id", savedId)
            .delete(BackendServerRoute.API_WORK_ID.route)
    }

    @Test
    fun `create with invalid data (failed)`() {
        given()
            .When {
                body(
                    """
               {
                   "title":"any title",
                   "description":"desc save",
                   "price":773.31,
                   "payDate":"2020-11-28aaa"
                }
            """
                        .trimIndent()
                )
                    .post(BackendServerRoute.API_WORK.route)
            }
            .Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            }
    }

    @Test
    fun `create without title (failed)`() {
        given()
            .When {
                body(
                    """
               {
                   "description":"desc save",
                   "price":773.31,
                   "payDate":"2020-11-28aaa"
                }
            """
                        .trimIndent()
                )
                    .post(BackendServerRoute.API_WORK.route)
            }
            .Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            }
    }

    @Test
    fun delete() {
        val titleDeleted = "title deleted 234112343215"

        // prepare work for to be deleted
        given()
            .body(
                """
                {
                   "title":"$titleDeleted",
                   "description":"desc save",
                   "price":773.31,
                   "payDate":"2020-11-28"
                }
            """
                    .trimIndent()
            ).post(BackendServerRoute.API_WORK.route)

        val getDeletedBody = given()
            .queryParam("title", titleDeleted)
            .get(BackendServerRoute.API_WORK.route)
            .body.asString()

        val deletedId = OBJECT_MAPPER.readTree(getDeletedBody)[0]["id"].asText()

        given()
            .pathParam("id", deletedId)
            .When {
                delete(BackendServerRoute.API_WORK_ID.route)
            }
            .Then {
                statusCode(HttpStatus.SC_NO_CONTENT)

                assertEquals(
                    HttpStatus.SC_NOT_FOUND,
                    given()
                        .pathParam("id", deletedId)
                        .get(BackendServerRoute.API_WORK_ID.route)
                        .statusCode
                )
            }
    }

    @Test
    fun `delete with not existence id (failed)`() {
        val workId = "2222cc21-a845-45d7-aea9-ab624172d1c1"
        given()
            .When {
                pathParam("id", workId)
                delete(BackendServerRoute.API_WORK_ID.route)
            }.Then {
                statusCode(HttpStatus.SC_NOT_FOUND)
            }
    }

    @Test
    fun `delete with invalid id (failed)`() {
        val workId = "b3542c"
        given()
            .When {
                pathParam("id", workId)
                delete(BackendServerRoute.API_WORK_ID.route)
            }.Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            }
    }

    private fun given() = Given {
        header("Content-type", "application/json")

        SecurityHelper.obtainPasswordGrantAccessToken().bearerAuthorizationHeader().let {
            header(it.headerName, it.headerValue)
        }
    }
}
