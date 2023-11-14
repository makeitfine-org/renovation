/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
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
            addAllowedMethod(HttpMethod.PUT)
            addAllowedMethod(HttpMethod.DELETE)
            addExposedHeader(CorsConfiguration.ALL)
            //play with prod (lite-server)
            addAllowedOriginPattern("http://192.168.0.113:3000")

            addAllowedOriginPattern("http://localhost:80*")
            // ng-part angular server default port
            addAllowedOriginPattern("http://localhost:4200")

            // needed for ng-part test (karma)
            addAllowedOriginPattern("http://localhost:98*")
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/graphql/**", config)
        source.registerCorsConfiguration("/api/v1/info/todo/**", config)
        source.registerCorsConfiguration("/about", config)
        source.registerCorsConfiguration("/module", config)

        return CorsFilter(source)
    }
}
