package renovation.temp.controller

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@Tags(
    Tag("integrationTest"),
    Tag("e2eTest")
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CoroutinesControllerTest(
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
