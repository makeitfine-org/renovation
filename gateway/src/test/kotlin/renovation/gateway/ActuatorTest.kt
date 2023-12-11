/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.gateway

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalManagementPort
import org.springframework.test.context.ContextConfiguration
import renovation.common.util.Json
import renovation.common.util.Rest.given

@Tag("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [ContainersConfig::class])
@Execution(ExecutionMode.CONCURRENT)
internal class ActuatorTest(
    @LocalManagementPort val managementPort: Int,
) {

    @ParameterizedTest
    @ValueSource(strings = ["/actuator", "/actuator/info", "/actuator/env", "/actuator/metrics"])
    fun checkUrlStatus(url: String) = given(managementPort)
        .When {
            get(url)
        }.Then {
            statusCode(SC_OK)
        }.let { }

    @Test
    fun `actuator health`() = given(managementPort)
        .When {
            get("/actuator/health")
        }.Then {
            statusCode(SC_OK)

            body("status", CoreMatchers.equalTo("UP"))

            body("components.diskSpace.status", CoreMatchers.equalTo("UP"))
            body("components.diskSpace.details.exists", CoreMatchers.equalTo(true))

            body("components.livenessState.status", CoreMatchers.equalTo("UP"))
            body("components.ping.status", CoreMatchers.equalTo("UP"))
            body("components.readinessState.status", CoreMatchers.equalTo("UP"))
        }.let {}

    @Test
    fun `actuator keycloakhealth`() = given(managementPort)
        .When {
            get("/actuator/keycloakhealth")
        }.Then {
            statusCode(SC_OK)

            body(
                CoreMatchers.equalTo(
                    Json.rowJson(
                        """
                        {
                          "status": "UP",
                          "checks": [
                            {
                              "name": "Keycloak database connections health check",
                              "status": "UP"
                            }
                          ]
                        }
                        """
                    )
                )
            )
        }.let {}
}
