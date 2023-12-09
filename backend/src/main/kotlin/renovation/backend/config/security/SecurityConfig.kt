/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.config.security

import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Suppress("detekt:UtilityClassWithPublicConstructor")
@KeycloakConfiguration
@ConditionalOnProperty(name = ["keycloak.enabled"], havingValue = "true", matchIfMissing = true)
class SecurityConfig { // : KeycloakWebSecurityConfigurerAdapter() {

    companion object {

        @JvmStatic
        @Throws(Exception::class)
        fun configureApiSecurity(http: HttpSecurity) =
            http
                .csrf {
                    it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    it.disable()
                }
                // .and()
                // todo: enable
                .authorizeHttpRequests {
                    it.requestMatchers("/about", "/project", "/logout").permitAll()
                        .requestMatchers("/api/work").hasAnyRole("WORK")
                        .requestMatchers("/api/worker").hasAnyRole("WORKER")
                        .requestMatchers("/*").hasAnyRole("ADMIN")
                        .anyRequest()
                        .authenticated()
                }
                .logout {
                    it.invalidateHttpSession(true)
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/logout-redirect")
                }
    }

//    @Throws(Exception::class)
//    override fun configure(http: HttpSecurity) {
//        super.configure(http)
//        configureApiSecurity(http)
//    }

//    @Autowired
//    fun configureGlobal(auth: AuthenticationManagerBuilder) =
//        auth.authenticationProvider(getKeycloakAuthenticationProvider())

//    private fun getKeycloakAuthenticationProvider() = keycloakAuthenticationProvider().also {
//        val mapper = SimpleAuthorityMapper()
//        mapper.setConvertToUpperCase(true)
//        it.setGrantedAuthoritiesMapper(mapper)
//    }
//
//    @Bean
//    override fun sessionAuthenticationStrategy() = RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
}
