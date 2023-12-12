/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend

import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@Suppress("UnnecessaryAbstractClass", "UtilityClassWithPublicConstructor")
internal abstract class AppByContainersConfig {

    companion object {

        @Container
        @ServiceConnection(name = "postgres")
        val postgresContainer: PostgreSQLContainer<Nothing> =
            PostgreSQLContainer<Nothing>("postgres:16.1-alpine")

        @Container
        @ServiceConnection(name = "redis")
        val redisContainer: GenericContainer<Nothing> =
            GenericContainer<Nothing>(DockerImageName.parse("redis:7.2.3-alpine"))
                .withExposedPorts(6379)
    }
}
