/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.controller

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import renovation.common.util.Json

@Tag("e2eTest")
@ActiveProfiles("vault")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class VaultControllerTest(
    @LocalServerPort private val port: Int,
) {
    @Test
    fun unblocked() {
        given()
            .When {
                get("vault")
            }.Then {
                body(
                    CoreMatchers.equalTo(
                        Json.rowJson(
                            """
                            [
                                "Next Hello!",
                                "Next there!!!"
                            ]
                            """.trimIndent()
                        )
                    )
                )
            }
    }

    fun given() = Given {
        port(port)
            .and().header("Content-type", "application/json")
    }
}
