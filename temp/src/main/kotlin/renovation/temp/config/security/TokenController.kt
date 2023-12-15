/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.config.security

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger { }

@RestController
@ConditionalOnProperty(name = ["spring.security.enabled"], havingValue = "true", matchIfMissing = true)
class TokenController(private val tokenService: TokenService) {

    @PostMapping("/token")
    fun token(authentication: Authentication): String {
        log.debug("Token requested for user: '{}'", authentication.name)
        val token = tokenService.generateToken(authentication)
        log.debug("Token granted: {}", token)
        return token
    }
}
