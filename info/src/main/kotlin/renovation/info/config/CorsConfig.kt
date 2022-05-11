/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {
    @Bean
    fun corsFilter(): CorsFilter {
        val config = CorsConfiguration().apply {
//            allowCredentials = false
            addAllowedHeader(CorsConfiguration.ALL)
            addAllowedMethod(HttpMethod.GET)
            addAllowedMethod(HttpMethod.POST)
            addExposedHeader(CorsConfiguration.ALL)
            addAllowedOriginPattern("http://localhost:80*")
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/graphql/**", config)

        return CorsFilter(source)
    }
}
