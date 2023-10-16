/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2023
 */

package renovation.gateway.config

import java.util.function.Supplier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExposeApiConfig {

    @Bean
    fun about() = Supplier<Any> {
        object {
            val name = "renovation gateway module"
            val description = "Gateway for routing/gathering different request"
        }
    }

    @Bean
    fun admin() = Supplier<String> {
        "admin"
    }

    @Bean
    fun user() = Supplier<String> {
        "user"
    }

    @Bean
    fun anonymous() = Supplier<String> {
        "anonymous"
    }

    @Bean
    fun unauthorized() = Supplier<String> {
        "unauthorized"
    }
}
