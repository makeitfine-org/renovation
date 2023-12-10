/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.gateway

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry

@TestConfiguration(proxyBeanMethods = false)
class ContainersConfig {

    @Bean
    fun keycloakContainer(registry: DynamicPropertyRegistry): KeycloakContainer =
        KeycloakContainer("quay.io/keycloak/keycloak:18.0.2")
            .withRealmImportFile("keycloak/renovation-realm-test.json")
            .also {
                registry.add("spring.security.oauth2.client.provider.oauth-client.issuer-uri") {
                    "${it.authServerUrl}/realms/renovation-realm"
                }
            }
}

fun main(args: Array<String>) {
    fromApplication<GatewayApplication>()
        .with(ContainersConfig::class.java)
        .run(*args)
}
