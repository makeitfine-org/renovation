package renovation.kafka.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StartupController(
    @Value("\${info.app.name}")
    private val appName: String,
    @Value("\${info.app.description}")
    private val appDesc: String
) {
    @GetMapping("/about")
    fun about(): Any = object {
        val name = appName
        val description = appDesc
    }

    @GetMapping("/module")
    fun index() = "Hi, it's \"${StringUtils.capitalize(appName)}\" module"
}
