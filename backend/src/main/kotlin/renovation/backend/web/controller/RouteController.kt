/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

const val FORWARD = "forward:/"
const val REDIRECT = "redirect:/"

@Controller
class RouteController {

    // todo: simplify it or move to separate config
    @RequestMapping("/home", "/fe", "/fe/**")
    // @RequestMapping("/{path:[^\\.]*}")
    fun forward() = FORWARD

    @RequestMapping("/logout-redirect")
    fun logoutRedirect() = REDIRECT
}
