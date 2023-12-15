package renovation.temp.controller

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import renovation.common.util.Rest.given

@Tags(
    Tag("integrationTest"),
    Tag("e2eTest")
)
@ActiveProfiles("no-security-no-vault")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CoroutinesControllerTest(
    @LocalServerPort val port: Int
) {
    @Test
    fun blocked() {
        given(port)
            .When {
                get("/coroutines/default")
            }.Then {
                body(CoreMatchers.equalTo("Result is being printed in app console ..."))
            }
    }

    @Test
    fun unblocked() {
        given(port)
            .When {
                get("/coroutines/blocking")
            }.Then {
                body(CoreMatchers.equalTo("Result is being printed in app console (blocking) ..."))
            }
    }
}
