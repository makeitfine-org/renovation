/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.controller

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import renovation.common.util.Json
import renovation.common.util.Rest.given

@Tag("e2eTest")
@ActiveProfiles("vault")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class VaultControllerTest(
    @LocalServerPort private val port: Int,
) {
    @Test
    fun unblocked() {
        given(port)
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
}
