/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.web

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
import renovation.backend.KeycloakContainerConfig
import renovation.backend.PostgresContainerConfig
import renovation.backend.RedisContainerConfig
import renovation.common.util.Json
import renovation.common.util.Rest

@Tag("integrationTest")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@ContextConfiguration(
    classes = [
        KeycloakContainerConfig::class,
        PostgresContainerConfig::class,
        RedisContainerConfig::class
    ]
)
@Execution(ExecutionMode.CONCURRENT)
internal class ActuatorTest(
    @LocalManagementPort val managementPort: Int,
) {

    @ParameterizedTest
    @ValueSource(
        strings = [
            "/actuator",
            // "/actuator/prometheus", // todo: figuring out why it's not working
            "/actuator/info",
            "/actuator/env",
            "/actuator/metrics"
        ]
    )
    fun checkUrlStatus(url: String) = Rest.given(managementPort)
        .When {
            get(url)
        }.Then {
            statusCode(SC_OK)
        }.let { }

    @Test
    fun `actuator health`() = Rest.given(managementPort)
        .When {
            get("/actuator/health")
        }.Then {
            statusCode(SC_OK)

            body("status", CoreMatchers.equalTo("UP"))

            body("components.db.status", CoreMatchers.equalTo("UP"))
            body("components.db.details.database", CoreMatchers.equalTo("PostgreSQL"))
            body("components.db.details.validationQuery", CoreMatchers.equalTo("isValid()"))

            body("components.redis.status", CoreMatchers.equalTo("UP"))
            body("components.redis.details.version", CoreMatchers.equalTo("7.2.3"))

            body("components.diskSpace.status", CoreMatchers.equalTo("UP"))
            body("components.diskSpace.details.exists", CoreMatchers.equalTo(true))

            body("components.livenessState.status", CoreMatchers.equalTo("UP"))
            body("components.ping.status", CoreMatchers.equalTo("UP"))
            body("components.readinessState.status", CoreMatchers.equalTo("UP"))
        }.let {}

    @Test
    fun `actuator keycloakhealth`() = Rest.given(managementPort)
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
