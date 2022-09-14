/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.backend.config.security

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import renovation.common.iam.impl.PasswordGrantAccessToken

private val log = KotlinLogging.logger { }

@Configuration
@Lazy
class InfoModuleConfig {

    @Bean
    fun accessToken(
        @Value("\${keycloak.auth-server-url}")
        authServerUrl: String,
        @Value("\${keycloak.realm}")
        realm: String,
        @Value("\${keycloak.resource}")
        clientId: String,
        @Value("\${keycloak.credentials.secret}")
        clientSecret: String,
        @Autowired
        creds: InfoServiceIamCredentials
    ) = PasswordGrantAccessToken(
        clientId,
        clientSecret,
        creds.username,
        creds.password,
        "${authServerUrl}/realms/$realm/protocol/openid-connect/token"
    ).also {
        log.debug { "accessToken: $it" }
    }

    @Configuration
    @ConfigurationProperties(prefix = "info-service.iam")
    class InfoServiceIamCredentials {
        lateinit var username: String
        lateinit var password: String
    }
}
