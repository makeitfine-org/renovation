/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2023
 */

package renovation.backend.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import renovation.common.security.jwt.JwtUtils

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(name = ["spring.security.enabled"], havingValue = "true", matchIfMissing = true)
class SecurityConfig(
    @Value("\${spring.security.oauth2.client.registration.oauth-client.client-id}")
    private val clientId: String,
    private val jwtDecoder: JwtDecoder,
    private val clientRegistrationRepository: ClientRegistrationRepository,

    @Value("\${server.host}")
    private val host: String,
    @Value("\${server.port}")
    private val port: Int
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf {
                it
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .disable()
            }
            .authorizeHttpRequests {
                it.requestMatchers("/about", "/project").permitAll()
                    .requestMatchers("/api/work/**").hasAnyRole("work")
                    .requestMatchers("/api/worker/**").hasAnyRole("worker")
                    .requestMatchers("/**").hasAnyRole("admin")
                    .anyRequest()
                    .authenticated()
            }.oauth2Login {
                it.userInfoEndpoint { userInfoEndpoint ->
                    userInfoEndpoint
                        .oidcUserService(oidcUserService())
                }
                it.authorizationEndpoint {
                    it.authorizationRequestResolver(pkceResolver())
                }
            }.oauth2ResourceServer { resourceServerConfigurer ->
                resourceServerConfigurer
                    .jwt { jwtConfigurer ->
                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())
                    }
            }
            .logout {
                it.invalidateHttpSession(true)
                it.logoutSuccessHandler(oidcLogoutSuccessHandler())
//                    .logoutUrl("/logout")
                it.logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                it.logoutSuccessUrl("/logout-redirect")
            }.build()

    @Bean
    fun pkceResolver() = OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI.let {
        DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, it).also {
            it.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce())
        }
    }

    /**
     * https://docs.spring.io/spring-security/reference/servlet/saml2/logout.html
     */
    private fun oidcLogoutSuccessHandler(): LogoutSuccessHandler {
        val oidcLogoutSuccessHandler = OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository)

        // Sets the location that the End-User's User Agent will be redirected to
        // after the logout has been performed at the Provider
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("http://$host:$port/about")

        return oidcLogoutSuccessHandler
    }

    @Bean
    fun oidcUserService(): OAuth2UserService<OidcUserRequest, OidcUser> {
        val delegate = OidcUserService()

        return OAuth2UserService { userRequest: OidcUserRequest ->
            val oidcUser = delegate.loadUser(userRequest)

            DefaultOidcUser(
                clientRolesAuthorities(
                    jwtDecoder.decode(userRequest.accessToken.tokenValue)
                ),
                oidcUser.idToken,
                oidcUser.userInfo
            )
        }
    }

    @Suppress("NestedBlockDepth")
    fun clientRolesAuthorities(jwt: Jwt) = JwtUtils.clientRolesAuthorities(clientId, jwt)

    @Bean
    fun jwtAuthenticationConverter() = JwtAuthenticationConverter().also {
        it.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter())
    }

    @Bean
    @Suppress("NestedBlockDepth")
    fun jwtGrantedAuthoritiesConverter() = JwtUtils.jwtGrantedAuthoritiesConverter(clientId)
}
