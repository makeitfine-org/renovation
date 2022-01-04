/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class RouteController {

    //todo: simplify it or move to separate config
    @RequestMapping("/work", "/home")
    //@RequestMapping("/{path:[^\\.]*}")
    fun redirect() = "forward:/"
}
