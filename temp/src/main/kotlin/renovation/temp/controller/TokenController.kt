/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.controller

import mu.KotlinLogging
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import renovation.temp.service.TokenService

private val log = KotlinLogging.logger { }

@RestController
class TokenController(private val tokenService: TokenService) {

    @PostMapping("/token")
    fun token(authentication: Authentication): String {
        log.debug("Token requested for user: '{}'", authentication.name)
        val token = tokenService.generateToken(authentication)
        log.debug("Token granted: {}", token)
        return token
    }
}
