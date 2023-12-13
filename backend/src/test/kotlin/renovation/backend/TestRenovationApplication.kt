/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend

import org.springframework.boot.fromApplication

fun main(args: Array<String>) {
    fromApplication<RenovationApplication>()
        .with(
            KeycloakContainerConfig::class.java,
            PostgresContainerConfig::class.java,
            RedisContainerConfig::class.java
        )
        .run(*args)
}
