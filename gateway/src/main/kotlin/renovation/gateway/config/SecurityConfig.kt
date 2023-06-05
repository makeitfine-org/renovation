/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.gateway.config

import com.nimbusds.jose.shaded.json.JSONArray
import com.nimbusds.jose.shaded.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(name = ["spring.security.enabled"], havingValue = "true", matchIfMissing = true)
class SecurityConfig(
    @Value("\${spring.security.oauth2.client.registration.oauth-client.client-id}")
    private val clientId: String,
    private val jwtDecoder: JwtDecoder,
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests {
            it
                .antMatchers("/about").permitAll()
                .antMatchers("/admin").hasAnyRole("admin")
                .antMatchers("/user").hasAnyRole("work", "worker", "admin")
                .anyRequest().authenticated()
        }.oauth2Login {
            it.userInfoEndpoint { userInfoEndpoint ->
                userInfoEndpoint
                    .oidcUserService(this.oidcUserService())
            }
        }.oauth2ResourceServer { resourceServerConfigurer ->
            resourceServerConfigurer
                .jwt { jwtConfigurer ->
                    jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
        }
    }

    @Bean
    fun oidcUserService(): OAuth2UserService<OidcUserRequest, OidcUser> {
        val delegate = OidcUserService()

        return OAuth2UserService { userRequest: OidcUserRequest ->
            val oidcUser = delegate.loadUser(userRequest)

            jwtDecoder.decode(userRequest.accessToken.tokenValue).let {
                clientRolesAuthorities(it).let {
                    DefaultOidcUser(it, oidcUser.idToken, oidcUser.userInfo)
                }
            }
        }
    }

    @Suppress("NestedBlockDepth")
    fun clientRolesAuthorities(jwt: Jwt): Collection<GrantedAuthority> {
        val grantedAuthorities: MutableCollection<GrantedAuthority> = mutableListOf()

        jwt.getClaim<Any>("resource_access")?.let {
            (it as? JSONObject)?.let {
                (it[clientId] as? JSONObject)?.let {
                    (it["roles"] as? JSONArray)?.let {
                        it.stream().forEach {
                            grantedAuthorities.add(SimpleGrantedAuthority("ROLE_${it.toString().lowercase()}"))
                        }
                    }
                }
            }
        }

        return grantedAuthorities
    }

    @Bean
    fun jwtAuthenticationConverter() = JwtAuthenticationConverter().also {
        it.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter())
    }

    @Bean
    @Suppress("NestedBlockDepth")
    fun jwtGrantedAuthoritiesConverter(): Converter<Jwt, Collection<GrantedAuthority>> {
        val delegate = JwtGrantedAuthoritiesConverter()

        return object : Converter<Jwt, Collection<GrantedAuthority>> {

            @Suppress("ReturnCount")
            override fun convert(jwt: Jwt): Collection<GrantedAuthority>? {
                val grantedAuthorities = delegate.convert(jwt)
                if (jwt.getClaim<Any>("realm_access") == null) {
                    return grantedAuthorities
                }
                val realmAccess = jwt.getClaim<JSONObject>("realm_access")
                if (realmAccess["roles"] == null) {
                    return grantedAuthorities
                }
                val roles = realmAccess["roles"] as JSONArray
                val keycloakAuthorities = roles.stream().map { role: Any ->
                    SimpleGrantedAuthority("ROLE_$role")
                }.toList()
                grantedAuthorities!!.addAll(keycloakAuthorities)

                addClientRoles(jwt, grantedAuthorities)

                return grantedAuthorities
            }

            private fun addClientRoles(jwt: Jwt, grantedAuthorities: MutableCollection<GrantedAuthority>) {
                jwt.getClaim<Any>("resource_access")?.let {
                    (it as? JSONObject)?.let {
                        (it[clientId] as? JSONObject)?.let {
                            (it["roles"] as? JSONArray)?.let {
                                val clientRoles = it.stream().map { r: Any ->
                                    SimpleGrantedAuthority("ROLE_${r.toString().lowercase()}")
                                }.toList()

                                grantedAuthorities.addAll(clientRoles)
                            }
                        }
                    }
                }
            }
        }
    }
}
