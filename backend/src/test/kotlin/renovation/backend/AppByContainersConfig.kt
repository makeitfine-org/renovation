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
internal abstract class AppByContainersConfig
