/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.gateway.config.certificate

import java.security.Principal
import org.springframework.context.annotation.Profile
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/certificate")
@Profile("certificate")
class Certificate {

    @GetMapping("/user")
    fun user(model: Model, principal: Principal): String {
        val principalInside = (principal as Authentication).principal
        val currentUser = if (principalInside is DefaultOAuth2User) {
            principalInside.attributes["login"] ?: "<NO USER>"
        } else {
            (principalInside as UserDetails).username
        }

        model.addAttribute("username", currentUser)

        return "Hello, $currentUser!"
    }
}
