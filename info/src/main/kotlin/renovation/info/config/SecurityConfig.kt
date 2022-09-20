package renovation.info.config

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
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@ConditionalOnProperty(name = ["spring.security.enabled"], havingValue = "true", matchIfMissing = true)
class SecurityConfig(
    @Value("\${spring.security.oauth2.client.registration.client.client-id}")
    private val clientId: String
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests { authorizeRequests ->
            authorizeRequests
                .antMatchers("/module").permitAll()
                .antMatchers("/graphiql", "/graphql").hasAnyRole("ADMIN", "SERVICE")
                .anyRequest().authenticated()
        }.oauth2ResourceServer { resourceServerConfigurer ->
            resourceServerConfigurer
                .jwt { jwtConfigurer ->
                    jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
        }
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

            fun addClientRoles(jwt: Jwt, grantedAuthorities: MutableCollection<GrantedAuthority>) {
                jwt.getClaim<Any>("resource_access")?.let {
                    (it as? JSONObject)?.let {
                        (it[clientId] as? JSONObject)?.let {
                            (it["roles"] as? JSONArray)?.let {
                                val clientRoles = it.stream().map { r: Any ->
                                    SimpleGrantedAuthority("ROLE_${r.toString().uppercase()}")
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
