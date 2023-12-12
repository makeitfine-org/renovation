/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2023
 */

package renovation.backend.config.security

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Scope
import renovation.common.security.iam.impl.ClientCredentialsGrantTypeAccessToken
import renovation.common.security.iam.impl.PasswordGrantTypeAccessToken

private val log = KotlinLogging.logger { }

@Configuration
@Lazy
class InfoModuleConfig {

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @ConditionalOnProperty(
        name = ["info-service.iam.clientCredentialsGrantType"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun clientCredentialsGrantAccessToken(
        @Value("\${spring.security.oauth2.client.provider.oauth-client.token-uri}")
        tokenUri: String,
        @Value("\${spring.security.oauth2.client.registration.oauth-client.client-id}")
        clientId: String,
        @Value("\${spring.security.oauth2.client.registration.oauth-client.client-secret}")
        clientSecret: String,
    ) = ClientCredentialsGrantTypeAccessToken(
        clientId,
        clientSecret,
        tokenUri,
    ).also {
        log.debug { "clientCredentialsGrantAccessToken: $it" }
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @ConditionalOnProperty(name = ["info-service.iam.passwordGrantType"], havingValue = "true", matchIfMissing = false)
    fun passwordGrantAccessToken(
        @Value("\${spring.security.oauth2.client.provider.oauth-client.token-uri}")
        tokenUri: String,
        @Value("\${spring.security.oauth2.client.registration.oauth-client.client-id}")
        clientId: String,
        @Value("\${spring.security.oauth2.client.registration.oauth-client.client-secret}")
        clientSecret: String,
        @Autowired
        creds: InfoServiceIamCredentials,
    ) = PasswordGrantTypeAccessToken(
        clientId,
        clientSecret,
        creds.username,
        creds.password,
        tokenUri
    ).also {
        log.debug { "passwordGrantAccessToken: $it" }
    }

    @ConditionalOnProperty(name = ["info-service.iam.passwordGrantType"], havingValue = "true", matchIfMissing = false)
    @Configuration
    @ConfigurationProperties(prefix = "info-service.iam")
    class InfoServiceIamCredentials {
        lateinit var username: String
        lateinit var password: String
    }
}
