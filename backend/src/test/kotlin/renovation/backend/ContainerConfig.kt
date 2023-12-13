/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer

@TestConfiguration(proxyBeanMethods = false)
internal class KeycloakContainerConfig {

    @Bean
    fun keycloakContainer(registry: DynamicPropertyRegistry): KeycloakContainer =
        KeycloakContainer("quay.io/keycloak/keycloak:23.0.1")
            .withEnv("KC_HEALTH_ENABLED", "true")
            .withRealmImportFile("keycloak/renovation-realm-test.json")
            .also {
                registry.add("keycloak.auth-server-url") {
                    "${it.authServerUrl}"
                }
            }
}

@TestConfiguration(proxyBeanMethods = false)
class PostgresContainerConfig {

    @Bean
    @ServiceConnection(name = "postgres")
    fun postgresContainer(): PostgreSQLContainer<*> = PostgreSQLContainer("postgres:16.1-alpine")
}

@TestConfiguration(proxyBeanMethods = false)
class RedisContainerConfig {

    @Bean
    @ServiceConnection(name = "redis")
    fun redisContainer(): GenericContainer<*> = GenericContainer("redis:7.2.3-alpine")
        .withExposedPorts(6379)
}
