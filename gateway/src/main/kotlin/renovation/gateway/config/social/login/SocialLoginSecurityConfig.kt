/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.gateway.config.social.login

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@Profile("social-login")
class SocialLoginSecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests(Customizer {
            it.requestMatchers("/about", "/unauthorized", "/anonymous").permitAll()
                .requestMatchers("/scope/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/anonymous").anonymous()
                .anyRequest().authenticated()
        })
            .oauth2Login(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults())
            .build()
    }
}
