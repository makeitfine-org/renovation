/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test.minikube

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import java.util.stream.Stream
import kotlin.test.assertEquals
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import renovation.backend.api.test.ApiTest
import renovation.common.util.Json.given
import renovation.common.util.Json.rowJson

@Tag("minikubeTest")
@Execution(ExecutionMode.CONCURRENT)
internal class MinikubeApiTest : ApiTest {

    companion object {

        @JvmStatic
        fun minikubeParams(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    MinikubeRoute.IP_BACKEND_ABOUT.route,
                    rowJson(
                        """
                        {
                            "name": "renovation backend module",
                            "description": "Main backend part of renovation project"
                        }
                        """.trimIndent()
                    )
                ),

                Arguments.of(
                    MinikubeRoute.IP_INFO_ABOUT.route,
                    rowJson(
                        """
                        {
                          "name": "renovation info module",
                          "description": "Module work as additional info directory"
                        }
                        """.trimIndent()
                    )
                ),

                Arguments.of(
                    MinikubeRoute.IP_FRONTEND_INFO_ABOUT.route,
                    """
                    {
                      "name": "renovation frontend-info module",
                      "description": "Node js based backend module for showing different info"
                    }
                    
                    """.trimIndent()
                ),

                Arguments.of(
                    MinikubeRoute.MMIB_ABOUT.route,
                    rowJson(
                        """
                        {
                            "name": "renovation backend module",
                            "description": "Main backend part of renovation project"
                        }
                        """.trimIndent()
                    )
                ),

                Arguments.of(
                    MinikubeRoute.MMII_ABOUT.route,
                    """
                    {
                      "name": "renovation frontend-info module",
                      "description": "Node js based backend module for showing different info"
                    }
                    
                    """.trimIndent()
                ),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("minikubeParams")
    fun `body content check`(route: String, expectedBody: String) = `page body content`(route, expectedBody)

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
