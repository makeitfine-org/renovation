/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web.controller

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@Suppress("UnnecessaryAbstractClass", "UtilityClassWithPublicConstructor")
internal abstract class FunctionalTestAbstract {
    companion object {
        private const val POSTGRES_DOCKER_IMAGE = "postgres:14.2-alpine"
        private const val REDIS_DOCKER_IMAGE = "redis:7.0.0-alpine"

        @Container
        val postgresContainer: PostgreSQLContainer<Nothing> =
            PostgreSQLContainer<Nothing>(POSTGRES_DOCKER_IMAGE)

        @Container
        val redisContainer: GenericContainer<Nothing> =
            GenericContainer<Nothing>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
                .withExposedPorts(6379)

        @Container
        val keycloakContainer: KeycloakContainer = KeycloakContainer("quay.io/keycloak/keycloak:18.0.2")
            .withRealmImportFile("keycloak/renovation-realm-test.json")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgresContainer.jdbcUrl }
            registry.add("spring.datasource.password") { postgresContainer.password }
            registry.add("spring.datasource.username") { postgresContainer.username }

            registry.add("spring.redis.host") { redisContainer.host }
            registry.add("spring.redis.port") { redisContainer.firstMappedPort }
            registry.add("spring.redis.password") { "" }

//            registry.add("keycloak.auth-server-url") { keycloakContainer.authServerUrl }
        }
    }
}
