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
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import renovation.backend.api.test.FileHelper
import renovation.backend.api.test.ServerRoute

@Tag("smoke")
class WorkControllerApiTest {

    private val objectMapper = ObjectMapper()

    @Test
    fun test() {
        given()
            .When {
                get(ServerRoute.API_WORK.route)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)

                val expected = FileHelper.WORK_CONTROLLER_GET_ALL_RESPONSE.fileContent

                body(CoreMatchers.equalTo(rowJson(expected)))
            }
    }

    private fun rowJson(prettyJson: String) = objectMapper.readTree(prettyJson).toString()

    private fun given() = Given {
        header("Content-type", "application/json")
    }
}