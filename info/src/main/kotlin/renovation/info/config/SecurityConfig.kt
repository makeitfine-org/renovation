package renovation.info.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import renovation.common.security.jwt.JwtUtils

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@ConditionalOnProperty(name = ["spring.security.enabled"], havingValue = "true", matchIfMissing = true)
class SecurityConfig(
    @Value("\${spring.security.oauth2.client.registration.client.client-id}")
    private val clientId: String
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http.authorizeHttpRequests {
            it
                .requestMatchers("/module", "/about", "/favicon.ico").permitAll()
                .requestMatchers("/graphiql", "/graphql").hasAnyRole("ADMIN", "SERVICE")
                .requestMatchers("/api/v1/info/todo/**").permitAll()
                .anyRequest().authenticated()
        }.csrf {
            it.ignoringRequestMatchers("/api/v1/info/todo/**")
        }.oauth2ResourceServer {
            it.jwt { jwtConfigurer ->
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
        JwtUtils.jwtGrantedAuthoritiesConverter(clientId, JwtUtils.RoleCase.UPPERCASE)
}
