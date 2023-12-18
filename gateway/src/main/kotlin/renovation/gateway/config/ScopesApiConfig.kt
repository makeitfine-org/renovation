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
class ScopesApiConfig {

    @GetMapping("accounts")
    fun accounts() = object : RequestAndUser {
        override val request = "accounts (GET)"
        override val user = "user (accounts)"
    }

    @PostMapping("accounts")
    fun accountsPost() = object : RequestAndUser {
        override val request = "accounts (POST)"
        override val user = "user (accounts)"
    }

    companion object {
        interface RequestAndUser {
            val user: String
            val request: String
        }

        val userName: String by lazy {
            (SecurityContextHolder
                .getContext()
                .authentication
                .principal as DefaultOidcUser).userInfo.preferredUsername
        }
    }
}
