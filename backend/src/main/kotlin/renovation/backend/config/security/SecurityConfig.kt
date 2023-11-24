/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.config.security

import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@KeycloakConfiguration
@ConditionalOnProperty(name = ["keycloak.enabled"], havingValue = "true", matchIfMissing = true)
class SecurityConfig : KeycloakWebSecurityConfigurerAdapter() {

    companion object {

        @JvmStatic
        @Throws(Exception::class)
        fun configureApiSecurity(http: HttpSecurity) =
            http
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // .and()
                .disable() // todo: enable
                .authorizeRequests()
                .antMatchers("/about", "/project", "/logout").permitAll()
                .antMatchers("/api/work").hasAnyRole("WORK")
                .antMatchers("/api/worker").hasAnyRole("WORKER")
                .antMatchers("/*").hasAnyRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout-redirect")
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        configureApiSecurity(http)
    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) =
        auth.authenticationProvider(getKeycloakAuthenticationProvider())

    private fun getKeycloakAuthenticationProvider() = keycloakAuthenticationProvider().also {
        val mapper = SimpleAuthorityMapper()
        mapper.setConvertToUpperCase(true)
        it.setGrantedAuthoritiesMapper(mapper)
    }

    @Bean
    override fun sessionAuthenticationStrategy() = RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
}
