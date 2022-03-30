/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.api.test.serverapi

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import renovation.backend.api.test.FileHelper
import renovation.backend.api.test.ServerRoute
import java.util.*

@Tag("smoke")
internal class WorkControllerApiTest {
    companion object {
        @JvmStatic
        private val objectMapper = ObjectMapper()
    }

    @Test
    fun `find all`() {
        given()
            .When {
                get(ServerRoute.API_WORK.route)
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
                get(ServerRoute.API_WORK.route)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)

                val expected = FileHelper.WORK_CONTROLLER_GET_TITLE_LIKE_RESPONSE.fileContent
                body(equalTo(rowJson(expected)))
            }
    }

    @Test
    fun `find list by title like which not existence (failed)`() {
        given()
            .queryParam("title", "ti${UUID.randomUUID()}")
            .When {
                get(ServerRoute.API_WORK.route)
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
            .pathParam("id", "5")
            .When {
                get(ServerRoute.API_WORK_ID.route)
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
                get(ServerRoute.API_WORK_ID.route)
            }
            .Then {
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)

                body(equalTo("Internal server error (500)"))
            }
    }

    @Test
    fun `find by not existence id (failed)`() {
        given()
            .pathParam("id", Int.MIN_VALUE)
            .When {
                get(ServerRoute.API_WORK_ID.route)
            }
            .Then {
                statusCode(HttpStatus.SC_NOT_FOUND)
            }
    }

    private fun rowJson(prettyJson: String) = objectMapper.readTree(prettyJson).toString()

    private fun given() = Given {
        header("Content-type", "application/json")
    }
}