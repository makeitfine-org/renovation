/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders

@Configuration
@ConditionalOnProperty(name = ["spring.security.enabled"], havingValue = "true", matchIfMissing = true)
class DecoderConfig(
    @Value("\${spring.security.oauth2.client.provider.oauth-client.issuer-uri}")
    private val issuerUri: String,
) {

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return JwtDecoders.fromIssuerLocation(issuerUri)
    }
}
