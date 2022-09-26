/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.gateway.config

import java.util.function.Supplier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExposeApiConfig {

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
}
