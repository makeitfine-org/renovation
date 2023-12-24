/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.gateway.config.certificate

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@Profile("certificate & truststore")
class CertificateSecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http.authorizeHttpRequests {
            it.anyRequest()
                .authenticated()
                .and()
                .x509()
                .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                .userDetailsService(userDetailsService())
        }.build()

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService {
            if (it == "Tom") {
                return@UserDetailsService User(
                    it,
                    "",
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER")
                )
            }
            throw UsernameNotFoundException("User not found!")
        }
    }
}
