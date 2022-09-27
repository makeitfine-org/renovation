/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

const val FORWARD = "forward:/"

@Controller
class RouteController {

    // todo: simplify it or move to separate config
    @RequestMapping("/home", "/work/*", "/add/work", "/worker")
    // @RequestMapping("/{path:[^\\.]*}")
    fun redirect() = FORWARD
}
