/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.config.security

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(name = ["spring.security.enabled"], havingValue = "true", matchIfMissing = true)
class TokenService(val encoder: JwtEncoder) {

    fun generateToken(authentication: Authentication): String {
        val now = Instant.now()
        val scope = authentication.authorities.stream()
            .map { it.authority }
            .collect(Collectors.joining(" "))
        val claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .subject(authentication.name)
            .claim("scope", scope)
            .build()

        return encoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }
}
