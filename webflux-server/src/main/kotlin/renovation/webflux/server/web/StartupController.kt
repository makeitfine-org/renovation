/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.webflux.server.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StartupController {
    @GetMapping("/about")
    fun about(): Any = object {
        val name = "renovation webflux server module"
        val description = "Webflux part of renovation project"
    }
}
