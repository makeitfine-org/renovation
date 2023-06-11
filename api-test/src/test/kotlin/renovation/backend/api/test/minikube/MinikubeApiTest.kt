/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test.frontend.info

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Tag
import renovation.backend.api.test.ApiTest
import renovation.backend.api.test.minikube.MinikubeRoute

@Tag("minikube")
@Disabled // make tests taged 'minikube' not executed
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
    fun `page content test`(url: String, expectedContent: String) {
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
