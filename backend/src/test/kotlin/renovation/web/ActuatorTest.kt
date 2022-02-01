/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.web

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus.SC_OK
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort


@Tag("smoke")
@Tag("healthCheck")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ActuatorTest(
    @LocalServerPort val port: Int
) {
    val portHost = "http://localhost:${port}"

    @Test
    fun `actuator health`() {
        When {
            get(portHost + "/actuator/health")
        }.Then {
            statusCode(SC_OK)
        }
    }

    @Test
    fun `actuator info`() {
        When {
            get(portHost + "/actuator/info")
        }.Then {
            statusCode(SC_OK)
        }
    }
}
