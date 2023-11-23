package renovation.temp.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import renovation.temp.data.service.CoroutinesService

@RestController
@RequestMapping("/coroutines")
@Suppress("detekt:all")
class CoroutinesController(private val coroutinesService: CoroutinesService) {

    @GetMapping
    fun hello() = "Hello from coroutines!"

    @GetMapping("/default")
    suspend fun default(): Mono<String> = coroutinesService.default()
}
