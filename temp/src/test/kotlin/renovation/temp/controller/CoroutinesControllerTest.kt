package renovation.temp.controller

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource

@Tag("integrationTest")
@TestPropertySource(
    properties =
    ["spring.autoconfigure.exclude=org.springframework.cloud.vault.config.VaultAutoConfiguration"]
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoroutinesControllerTest(
    @LocalServerPort val port: Int
) {
    @Test
    fun blocked() {
        given()
            .When {
                get("/coroutines/default")
            }.Then {
                body(CoreMatchers.equalTo("Result is being printed in app console ..."))
            }
    }

    @Test
    fun unblocked() {
        given()
            .When {
                get("/coroutines/blocking")
            }.Then {
                body(CoreMatchers.equalTo("Result is being printed in app console (blocking) ..."))
            }
    }

    fun given() = Given {
        port(port)
            .and().header("Content-type", "application/json")
    }
}
