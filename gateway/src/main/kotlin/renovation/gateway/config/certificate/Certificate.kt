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
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/certificate")
@Profile("certificate")
class Certificate {

    @GetMapping
    fun user(model: Model, principal: Principal): String {
        val currentUser = (principal as Authentication).principal as UserDetails
        model.addAttribute("username", currentUser)
        return "user"
    }
}
