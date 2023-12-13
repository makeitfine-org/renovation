/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Suppress("UnnecessaryAbstractClass", "UtilityClassWithPublicConstructor")
@ContextConfiguration(
    classes = [
        PostgresContainerConfig::class,
        RedisContainerConfig::class,
    ]
)
//@Testcontainers
internal abstract class AppByContainersConfig // {
//    companion object {
//
//        @Container
//        @ServiceConnection(name = "postgres")
//        val postgresContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:16.1-alpine")
//
//        @Container
//        @ServiceConnection(name = "redis")
//        val redisContainer: GenericContainer<*> = GenericContainer("redis:7.2.3-alpine")
//            .withExposedPorts(6379)
//
//        @Container
//        val keycloakContainer: KeycloakContainer =
//            KeycloakContainer("quay.io/keycloak/keycloak:23.0.1")
//                .withEnv("KC_HEALTH_ENABLED", "true")
//                .withRealmImportFile("keycloak/renovation-realm-test.json")
//
//        @JvmStatic
//        @DynamicPropertySource
//        fun properties(registry: DynamicPropertyRegistry) {
//            registry.add("keycloak.auth-server-url") {
//                "${keycloakContainer.authServerUrl}"
//            }
//        }
//    }
// }
