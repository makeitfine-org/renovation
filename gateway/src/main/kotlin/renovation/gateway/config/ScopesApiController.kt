/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.gateway.config

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/scope")
class ScopesApiController {

    @GetMapping("accounts")
    fun accounts() = obj("accounts (GET)", "user (accounts)")

    @PostMapping("accounts")
    fun accountsPost() = obj("accounts (POST)", "user (accounts)")

    @GetMapping("bots")
    fun bots() = obj("bots (GET)", "user (bots)")

    @PostMapping("bots")
    fun botsPost() = obj("bots (POST)", "user (bots)")

    @GetMapping("reports")
    fun reports() = obj("reports (GET)", "user (reports)")

    @PostMapping("reports")
    fun reportsPost() = obj("reports (POST)", "user (reports)")

    companion object {
        interface RequestAndUser {
            val user: String
            val request: String
        }

        @Suppress("UnusedPrivateProperty")
        private val userName: String by lazy {
            (
                SecurityContextHolder
                    .getContext()
                    .authentication
                    .principal as DefaultOidcUser
                )
                .userInfo.preferredUsername
        }

        @JvmStatic
        private fun obj(request: String, user: String) = object : RequestAndUser {
            override val request = request
            override val user = user
        }
    }
}
