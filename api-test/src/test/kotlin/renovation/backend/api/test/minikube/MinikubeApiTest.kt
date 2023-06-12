/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test.minikube

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import kotlin.test.assertEquals
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import renovation.backend.api.test.ApiTest
import renovation.common.util.Json.given
import renovation.common.util.Json.rowJson

@Tag("minikube")
@Execution(ExecutionMode.CONCURRENT)
internal class MinikubeApiTest : ApiTest {

    @Test
    fun `backend about`() = `page body content (json formatted)`(
        MinikubeRoute.BACKEND_ABOUT.route,
        """
        {
            "name": "renovation backend module",
            "description": "Main backend part of renovation project"
        }
        """.trimIndent()
    )

    @Test
    fun `info about`() = `page body content (json formatted)`(
        MinikubeRoute.INFO_ABOUT.route,
        """
        {
          "name": "renovation info module",
          "description": "Module work as additional info directory"
        }
        """.trimIndent()
    )

    @Test
    fun `frontend info about`() = `page body content`(
        MinikubeRoute.FRONTEND_INFO_ABOUT.route,
        """
        {
          "name": "renovation frontend-info module",
          "description": "Node js based backend module for showing different info"
        }
        
        """.trimIndent()
    )

    private fun `page body content (json formatted)`(url: String, expectedBody: String) =
        `page body content`(url, rowJson(expectedBody))

    private fun `page body content`(url: String, expectedBody: String) {
        given()
            .When {
                get(url)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)

                val actualBody = extract().body().asString()

                assertEquals(expectedBody, actualBody)
            }
    }
}
