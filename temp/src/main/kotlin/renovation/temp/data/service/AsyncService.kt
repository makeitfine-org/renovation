/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.data.service

import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger { }
const val SIMPLE_WAIT = 3000L

@Service
class AsyncService {
    fun get(): String {
        asyncMethodWithVoidReturnType()
        return "abc"
    }

    @Async("threadPoolTaskExecutor")
    fun asyncMethodWithVoidReturnType() {
        Thread.sleep(SIMPLE_WAIT)
        log.debug { "Execute method asynchronously. ${Thread.currentThread().name}" }
    }
}
