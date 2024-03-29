package renovation.info.web.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StartupController(
    @Value("\${spring.application.name}")
    private val applicationName: String
) {

    @GetMapping("/about")
    fun about(): Any = object {
        val name = "renovation info module"
        val description = "Module work as additional info directory"
    }

    @GetMapping("/module")
    fun index() = "Hi, it's \"${StringUtils.capitalize(applicationName)}\" module"
}
