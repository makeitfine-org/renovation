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
import org.junit.jupiter.api.Tag
import renovation.backend.api.test.FileHelper
import renovation.backend.api.test.SecurityHelper
import renovation.backend.api.test.ServerRoute
import kotlin.test.Test

@Tag("smoke")
internal class WorkerControllerApiTest {
    companion object {
        @JvmStatic
        private val OBJECT_MAPPER = ObjectMapper()
    }

    @Test
    fun `find all`() {
        given()
            .When {
                get(ServerRoute.API_WORKER.route)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)

                val expected = FileHelper.WORKER_CONTROLLER_GET_ALL_RESPONSE.fileContent
                body(equalTo(rowJson(expected)))
            }
    }

    // todo: move to util module (to think to create it previously)
    private fun rowJson(prettyJson: String) = OBJECT_MAPPER.readTree(prettyJson).toString()

    private fun given() = Given {
        header("Content-type", "application/json")

        SecurityHelper.getPasswordGrantAccessToken().bearerAuthorizationHeader().let {
            header(it.headerName, it.headerValue)
        }
    }
}
