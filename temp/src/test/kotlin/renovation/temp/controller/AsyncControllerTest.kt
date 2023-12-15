package renovation.temp.controller

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import renovation.common.util.Rest.given

@Tag("integrationTest")
@ActiveProfiles("no-security-no-vault")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AsyncControllerTest(
    @LocalServerPort val port: Int
) {
    @Test
    fun get() {
        given(port)
            .When {
                get("/async")
            }.Then {
                body(CoreMatchers.equalTo("abc"))
            }
    }

    @Test
    fun job() {
        given(port)
            .When {
                get("/async/job")
            }.Then {
                body(CoreMatchers.equalTo("Result in console of app"))
            }
    }
}
