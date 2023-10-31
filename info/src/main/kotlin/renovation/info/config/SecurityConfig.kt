package renovation.info.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import renovation.common.security.jwt.JwtUtils

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@ConditionalOnProperty(name = ["spring.security.enabled"], havingValue = "true", matchIfMissing = true)
class SecurityConfig(
    @Value("\${spring.security.oauth2.client.registration.client.client-id}")
    private val clientId: String
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http.authorizeRequests { authorizeRequests ->
            authorizeRequests
                .requestMatchers("/module", "/about").permitAll()
                .requestMatchers("/graphiql", "/graphql").hasAnyRole("ADMIN", "SERVICE")
                .requestMatchers("/api/v1/info/todo/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().ignoringRequestMatchers("/api/v1/info/todo/**")
        }.oauth2ResourceServer { resourceServerConfigurer ->
            resourceServerConfigurer
                .jwt { jwtConfigurer ->
                    jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
        }.build()

    @Bean
    fun jwtAuthenticationConverter() = JwtAuthenticationConverter().also {
        it.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter())
    }

    @Bean
    @Suppress("NestedBlockDepth")
    fun jwtGrantedAuthoritiesConverter() =
        JwtUtils.jwtGrantedAuthoritiesConverter(clientId, JwtUtils.ROLE_CASE.UPPERCASE)
}
