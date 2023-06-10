/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test.backend

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Tag
import renovation.backend.api.test.helper.SecurityHelper

@Tag("healthCheck")
internal class ActuatorCheckTest {

    @Test
    fun `actuator health`() {
        given()
            .When {
                get(BackendServerRoute.ACTUATOR_HEALTH.route)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)
            }
    }

    @Test
    fun `actuator info`() {
        given()
            .When {
                get(BackendServerRoute.ACTUATOR_INFO.route)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)
            }
    }

    private fun given() = Given {
        header("Content-type", "application/json")

        SecurityHelper.obtainPasswordGrantAccessToken().bearerAuthorizationHeader().let {
            header(it.headerName, it.headerValue)
        }
    }
}
