/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.gateway.config

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Tag("integration")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ExposeApiConfigIntegrationTest(
    @LocalServerPort private val port: Int,
) : ExposeApiConfigTestAbstract(port) {

    companion object {

        @Container
        val keycloakContainer: KeycloakContainer = KeycloakContainer("quay.io/keycloak/keycloak:18.0.2")
            .withRealmImportFile("keycloak/renovation-realm-test.json")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("keycloak.auth-server-url") { keycloakContainer.authServerUrl }
            registry.add("spring.security.oauth2.client.provider.oauth-client.token-uri") {
                "${keycloakContainer.authServerUrl}realms/renovation-realm/protocol/openid-connect/token"
            }
            registry.add("spring.security.oauth2.client.provider.oauth-client.issuer-uri") {
                "${keycloakContainer.authServerUrl}realms/renovation-realm"
            }
        }
    }
}
