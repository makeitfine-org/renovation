/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.gateway.config

import java.util.stream.Collectors
import net.minidev.json.JSONArray
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(name = ["spring.security.enabled"], havingValue = "true", matchIfMissing = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests { authorizeRequests ->
            authorizeRequests
                .antMatchers("/about").permitAll()
                .antMatchers("/admin", "/user").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
        }.oauth2Login { oauth2Login ->
            oauth2Login.userInfoEndpoint { userInfoEndpoint ->
                userInfoEndpoint
                    .oidcUserService(this.oidcUserService())
            }
        }
    }

    @Bean
    fun oidcUserService(): OAuth2UserService<OidcUserRequest, OidcUser> {
        val delegate = OidcUserService()

        return OAuth2UserService { userRequest: OidcUserRequest ->
            val oidcUser = delegate.loadUser(userRequest)
            val claims = oidcUser.claims
            val groups = claims["groups"] as JSONArray?
            val mappedAuthorities = groups?.let {
                it.stream().map { role ->
                    SimpleGrantedAuthority("ROLE_$role")
                }
                    .collect(Collectors.toSet())
            } ?: listOf()

            DefaultOidcUser(mappedAuthorities, oidcUser.idToken, oidcUser.userInfo)
        }
    }
}
