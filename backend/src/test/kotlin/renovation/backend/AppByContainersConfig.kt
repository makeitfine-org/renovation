/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@Suppress("UnnecessaryAbstractClass", "UtilityClassWithPublicConstructor")
internal abstract class AppByContainersConfig {

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

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgresContainer.jdbcUrl }
            registry.add("spring.datasource.password") { postgresContainer.password }
            registry.add("spring.datasource.username") { postgresContainer.username }

            registry.add("spring.redis.host") { redisContainer.host }
            registry.add("spring.redis.port") { redisContainer.firstMappedPort }
            registry.add("spring.redis.password") { "" }
        }
    }
}
