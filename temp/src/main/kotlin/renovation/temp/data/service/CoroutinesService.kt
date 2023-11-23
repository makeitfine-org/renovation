/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.data.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger { }

@Service
class CoroutinesService {
    suspend fun default() = Mono.just(coroutine())
}

suspend fun coroutine() = coroutineScope {
    launch {
        delayed()
    }
    val r = "Result is being printed in app console ..."
    log.debug { r }
    r
}

@Suppress("detekt:MagicNumber")
suspend fun delayed() {
    for (i in 0..5) {
        delay(300L)
        log.debug { i }
    }
}
