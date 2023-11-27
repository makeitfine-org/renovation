package renovation.temp.controller

import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import renovation.temp.data.service.AsyncService

private val log = KotlinLogging.logger { }

@RestController
@RequestMapping("/async")
class AsyncController(private val asyncService: AsyncService) {

    @GetMapping()
    fun get() = asyncService.get().also { log.debug { "get" } }

    @GetMapping("/job")
    fun job() = asyncService.asyncJot().let { "Result in console of app" }

    @GetMapping("/async_result")
    @Async
    fun getResultAsync(): CompletableFuture<*> {
        // sleep for 500 ms
        @Suppress("detekt:MagicNumber")
        Thread.sleep(500)
        log.debug { "getResultAsyc" }
        return CompletableFuture.completedFuture("Result is ready!")
    }
}

@Configuration
class AsyncConfig {

    @Bean
    fun threadPoolTaskExecutor() = ThreadPoolTaskExecutor()
}
