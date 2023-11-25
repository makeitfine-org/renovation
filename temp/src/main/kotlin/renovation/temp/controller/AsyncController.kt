package renovation.temp.controller

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import renovation.temp.data.service.AsyncService

@RestController
@RequestMapping("/async")
class AsyncController(private val asyncService: AsyncService) {

    @GetMapping()
    fun get() = asyncService.get()
}

@Configuration
class AsyncConfig {

    @Bean
    fun threadPoolTaskExecutor() = ThreadPoolTaskExecutor()
}
