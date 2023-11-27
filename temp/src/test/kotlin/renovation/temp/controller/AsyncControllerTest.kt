package renovation.temp.controller

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@Tag("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AsyncControllerTest(
    @LocalServerPort val port: Int
) {
    @Test
    fun get() {
        given()
            .When {
                get("/async")
            }.Then {
                body(CoreMatchers.equalTo("abc"))
            }
    }

    @Test
    fun job() {
        given()
            .When {
                get("/async/job")
            }.Then {
                body(CoreMatchers.equalTo("Result in console of app"))
            }
    }

    fun given() = Given {
        port(port)
            .and().header("Content-type", "application/json")
    }
}
