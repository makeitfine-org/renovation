/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend

import org.springframework.test.context.ContextConfiguration

@Suppress("UnnecessaryAbstractClass", "UtilityClassWithPublicConstructor")
@ContextConfiguration(
    classes = [
        PostgresContainerConfig::class,
        RedisContainerConfig::class,
    ]
)
internal abstract class AppByContainersConfig {

    // todo: change to ContainerConfig
//    companion object {
//
//        @Container
//        @ServiceConnection(name = "postgres")
//        val postgresContainer: PostgreSQLContainer<Nothing> =
//            PostgreSQLContainer<Nothing>("postgres:16.1-alpine")
//
//        @Container
//        @ServiceConnection(name = "redis")
//        val redisContainer: GenericContainer<Nothing> =
//            GenericContainer<Nothing>(DockerImageName.parse("redis:7.2.3-alpine"))
//                .withExposedPorts(6379)
//    }
}
