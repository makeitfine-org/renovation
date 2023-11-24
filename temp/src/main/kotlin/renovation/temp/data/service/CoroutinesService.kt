/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.data.service

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger { }

@Service
@Suppress("detekt:MagicNumber")
class CoroutinesService {

    companion object {
        @JvmStatic
        val th = newSingleThreadContext("one")
    }

    suspend fun default() = coroutine()

    fun coroutineBlocking() = runBlocking {
        launch {
            delayed()
        }
        val r = "Result is being printed in app console (blocking) ..."
        log.debug { r }
        r
    }

    private suspend fun coroutine() = coroutineScope {

        val job = launch(start = CoroutineStart.LAZY) {
            try {
                delayed()
                1
            } catch (e: CancellationException) {
                log.debug { "!!! ${e.message}" }
            } finally {
                log.debug { "finally" }
            }
        }

        job.start()
        delay(700)

        job.cancel(CancellationException("one!!!"))
//        job.cancel()

        log.debug { ">>> ${Thread.currentThread().name}" }

//        job.join()

        runBlocking(th) {
            log.debug { "> ${Thread.currentThread().id}" }
            log.debug { "inside 1 sec ..." }
            delay(1000)
        }
        val r = "Result is being printed in app console ..."
        log.debug { r }

        val printHello = async { printHello() }
        val printHello2 = async { printHello() }
        val printHello3 = async { printHello() }

        val v = "${printHello.await()}"
        "${printHello3.await()}"
        "${printHello2.await()}"
        log.debug { v }

        job?.start()
        r
    }

    private suspend fun printHello(): String {
        delay(500L)  // имитация продолжительной работы
        return "Hello work!"
    }

    @Suppress("detekt:MagicNumber")
    private suspend fun delayed() {
        for (i in 0..5) {
            delay(300L)
            log.debug { i }
        }
    }
}
