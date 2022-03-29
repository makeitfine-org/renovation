/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StartupController(
    @Value("\${spring.application.name}")
    private val applicationName: String
) {
    @GetMapping("/project")
    fun index() = "Hi, it's \"${StringUtils.capitalize(applicationName)}\" project"
}
