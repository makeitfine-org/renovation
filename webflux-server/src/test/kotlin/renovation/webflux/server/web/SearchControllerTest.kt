package renovation.webflux.server.web

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import renovation.common.util.Json
import renovation.common.util.Rest.given

@Tag("e2eTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchControllerTest(
    @LocalServerPort val port: Int
) {

    @Test
    fun searchByNamesTest() {
        given(port)
            .When {
                queryParam("query", "pa")
                get("/api/search")
            }.Then {
                body(
                    CoreMatchers.equalTo(
                        Json.rowJson(
                            """
                            [
                              {
                                "id": 1,
                                "name": "Company 1",
                                "type": "COMPANY"
                              },
                              {
                                "id": 2,
                                "name": "Company 2",
                                "type": "COMPANY"
                              },
                              {
                                "id": 3,
                                "name": "Company 3",
                                "type": "COMPANY"
                              }
                            ]
                            """.trimIndent()
                        )
                    )
                )
            }
    }
}
