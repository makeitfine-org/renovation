package renovation.gateway.config

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import renovation.common.util.Json
import renovation.common.util.Rest.given
import renovation.gateway.ContainersConfig

@Tag("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [ContainersConfig::class])
internal class ScopesApiControllerTest(@LocalServerPort private val port: Int) {

    @Test
    fun accounts() {
        request("get", "accounts")
    }

    @Test
    fun accountsPost() {
        request("post", "accounts")
    }

    @Test
    fun bots() {
        request("get", "bots")
    }

    @Test
    fun botsPost() {
        request("post", "bots")
    }

    @Test
    fun reports() {
        request("get", "reports")
    }

    @Test
    fun reportsPost() {
        request("post", "reports")
    }

    /**
     * method - get or post
     */
    private fun request(method: String, entity: String, user: String = entity) =
        given(port)
            .When {
                if (method == "get") {
                    get("/scope/$entity")
                } else {
                    post("/scope/$entity")
                }
            }.Then {
                statusCode(HttpStatus.SC_OK)

                body(
                    CoreMatchers.equalTo(
                        Json.rowJson(
                            """
                            {
                                "request": "$entity (${method.uppercase()})",
                                "user": "user ($user)"
                            }
                            """
                        )
                    )
                )
            }
}
