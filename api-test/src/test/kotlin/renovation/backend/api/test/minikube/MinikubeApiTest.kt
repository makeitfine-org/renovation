/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test.minikube

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Tag
import renovation.backend.api.test.ApiTest

@Tag("minikube")
internal class MinikubeApiTest : ApiTest {

    @Test
    fun `backend about`() = `page content test`(
        MinikubeRoute.BACKEND_ABOUT.route,
        """
        {
            "name": "renovation backend module",
            "description": "Main backend part of renovation project"
        }
        """.trimIndent()
    )

    @Test
    fun `info about`() = `page content test`(
        MinikubeRoute.INFO_ABOUT.route,
        """
        {
            "name": "renovation info module",
            "description": "Module work as additional info directory"
        }
        """.trimIndent()
    )

    @Test
    fun `frontend info about`() = `page content test`(
        MinikubeRoute.FRONTEND_INFO_ABOUT.route,
        """
        {
            "name": "renovation frontend-info module",
            "description": "Node js based backend module for showing different info"
        }
        """.trimIndent()
    )

    private fun `page content test`(url: String, expectedContent: String) {
        given()
            .When {
                get(url)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)

                body(
                    equalTo(
                        rowJson(
                            expectedContent
                        )
                    )
                )
            }
    }

    // todo: move to util module (to think to create it previously)
    private fun rowJson(prettyJson: String) = OBJECT_MAPPER.readTree(prettyJson).toString()

    private fun given() = Given {
        header("Content-type", "application/json")
    }
}
