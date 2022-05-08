/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus.SC_OK
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import renovation.backend.IntegrationTest
import kotlin.test.Test

@IntegrationTest
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["management.health.redis.enabled=false"]
)
internal class ActuatorTest(
    @LocalServerPort val port: Int
) {
    val portHost = "http://localhost:$port"

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
