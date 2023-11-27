package renovation.webflux.server.web

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import renovation.common.util.Json

@Tag("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class StartupControllerTest(
    @LocalServerPort val port: Int
) {
    @Test
    fun get() {
        given()
            .When {
                get("/about")
            }.Then {
                body(
                    CoreMatchers.equalTo(
                        Json.rowJson(
                            """
                            {
                                "name" : "renovation webflux server module",
                                "description" : "Webflux part of renovation project"
                            }
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
