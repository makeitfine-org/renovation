/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.api.test.serverapi

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Tag
import renovation.backend.api.test.ServerRoute
import kotlin.test.Test

@Tag("healthCheck")
internal class ActuatorCheckTest {

    @Test
    fun `actuator health`() {
        Given {
            header("Content-type", "application/json")
        }
        When {
            get(ServerRoute.ACTUATOR_HEALTH.route)
        }.Then {
            statusCode(HttpStatus.SC_OK)
        }
    }

    @Test
    fun `actuator info`() {
        Given {
            header("Content-type", "application/json")
        }
        When {
            get(ServerRoute.ACTUATOR_INFO.route)
        }.Then {
            statusCode(HttpStatus.SC_OK)
        }
    }
}