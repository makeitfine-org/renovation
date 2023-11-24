package renovation.temp.controller

import kotlinx.coroutines.DelicateCoroutinesApi
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import renovation.temp.data.service.CoroutinesService

private val log = KotlinLogging.logger { }

@RestController
@RequestMapping("/coroutines")
@Suppress("detekt:all")
class CoroutinesController(private val coroutinesService: CoroutinesService) {

    @GetMapping
    fun hello() = "Hello from coroutines!"

    @DelicateCoroutinesApi
    @GetMapping("/default")
    suspend fun default(): String {
        val r = coroutinesService.default()
        log.debug { "Controller: $r" }
        return r
    }

    @GetMapping("/blocking")
    fun blocking(): String {
        val r = coroutinesService.coroutineBlocking()
        log.debug { "Controller: $r" }
        return r
    }
}
