/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.web

import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import renovation.backend.PostgresContainerConfig
import renovation.backend.RedisContainerConfig

@Tag("integrationTest")
@ContextConfiguration(
    classes = [
        PostgresContainerConfig::class,
        RedisContainerConfig::class
    ]
)
@ActiveProfiles("no-security")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CounterStartupControllerTest(
    @LocalServerPort val port: Int
) {

    @BeforeTest
    fun init() {
        RestAssured.port = port
    }

    @Test
    fun `counter`() {
        When {
            get("/counter")
        }.Then {
            statusCode(HttpStatus.SC_OK)
            body(CoreMatchers.equalTo("1"))
        }
    }
}
