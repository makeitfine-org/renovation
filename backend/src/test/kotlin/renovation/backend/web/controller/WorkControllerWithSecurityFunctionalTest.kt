/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web.controller

import dasniko.testcontainers.keycloak.KeycloakContainer
import io.restassured.module.kotlin.extensions.Given
import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import renovation.common.iam.GrantTypeAccessToken
import renovation.common.iam.impl.PasswordGrantAccessToken

@Tag("functional")
@ActiveProfiles("secured-functional-test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
internal class WorkControllerWithSecurityFunctionalTest(
    @LocalServerPort private val port: Int,
) : WorkControllerFunctionalTestAbstract(port) {

    @TestConfiguration
    class ControllerTestConfig {

        @Bean
        fun token(
            @Value("\${keycloak.realm}")
            realm: String,
            @Value("\${keycloak.resource}")
            clientId: String,
            @Value("\${keycloak.credentials.secret}")
            clientSecret: String,
            apiWork: ApiWork,
        ) = PasswordGrantAccessToken(
            clientId,
            clientSecret,
            apiWork.username,
            apiWork.password,
            "${keycloakContainer.authServerUrl}/realms/$realm/protocol/openid-connect/token"
        )
    }

    companion object {

        @Container
        val keycloakContainer: KeycloakContainer = KeycloakContainer("quay.io/keycloak/keycloak:18.0.2")
            .withRealmImportFile("keycloak/renovation-realm-test.json")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            FunctionalTestAbstract.properties(registry)

            registry.add("keycloak.auth-server-url") { keycloakContainer.authServerUrl }
        }
    }

    @Autowired
    private lateinit var token: GrantTypeAccessToken

    override fun given() = Given {
        val header = token.bearerAuthorizationHeader()

        super.given()
            .and()
            .header(header.headerName, header.headerValue)
    }
}

@Configuration
@ConfigurationProperties(prefix = "iam.api-work")
class ApiWork {
    lateinit var username: String
    lateinit var password: String
}