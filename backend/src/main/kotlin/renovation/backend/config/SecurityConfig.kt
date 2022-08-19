/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.config

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy

@EnableWebSecurity
class SecurityConfig : KeycloakWebSecurityConfigurerAdapter() {

    @Bean
    override fun sessionAuthenticationStrategy() = RegisterSessionAuthenticationStrategy(SessionRegistryImpl())

    @Bean
    fun keycloakConfigResolver() = KeycloakSpringBootConfigResolver()

    override fun configure(auth: AuthenticationManagerBuilder) {
        val keycloakAuthenticationProvider = keycloakAuthenticationProvider()
        auth.authenticationProvider(keycloakAuthenticationProvider)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http
            .csrf()
            .disable() //todo: csrf token should be actived
//            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
            .authorizeRequests()
            .antMatchers("/project", "/").permitAll()
            .anyRequest()
            .authenticated()
    }
}
