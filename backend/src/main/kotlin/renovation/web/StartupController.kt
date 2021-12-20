package renovation.web

import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
class StartupController(
    @Value("\${spring.application.name}")
    private val applicationName: String
) {

    @GetMapping("/")
    fun index() = "Hi, it's \"${StringUtils.capitalize(applicationName)}\" project"
}
