/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.kotlin

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.assertj.core.api.Assertions

class KotlinCoroutines {

    @Test
    fun blocked() = runTest {
        coroutineScope {
            var counter = 0
            withTimeout(20010) {
                repeat(5) {
                    delay(4000L)
                    counter++
                }
            }

            assertEquals(5, counter)
        }
    }

    @Test
    fun blockedTimeout() = runTest {
        assertFailsWith<TimeoutCancellationException> {
            coroutineScope {
                var counter = 0
                withTimeout(20) {
                    repeat(5) {
                        delay(4000L)
                        counter++
                    }
                }

                assertEquals(5, counter)
            }
        }
    }

    @Test
    fun launchVar1() = runTest {
        var counter = 0
        launch {
            repeat(5) {
                delay(4000L)
                counter++
            }
            assertEquals(5, counter)
        }

        assertEquals(0, counter)
    }

    @Test
    fun launchVar2() = runTest {
        withTimeout(20010) {
            var counter = 0
            launch {
                repeat(5) {
                    delay(4000L)
                    counter++
                }
                assertEquals(5, counter)
            }

            assertEquals(0, counter)
        }
    }

    suspend fun work(): Int {
        repeat(5) {
            delay(4000L)
        }
        return 5
    }

    @Test
    fun launch() = runTest {
        var counter = 0
        launch {
            counter = work()
            assertEquals(5, counter)
        }

        assertEquals(0, counter)
    }

    @Test
    fun launchVar3() = runTest {
        var counter = 0
        withTimeout(20010) {
            launch {
                repeat(5) {
                    delay(4000L)
                    counter++
                }
            }

            launch {
                repeat(5) {
                    delay(4000L)
                    counter++
                }
            }

            assertEquals(0, counter)
        }

        assertEquals(10, counter)
    }

    @Test
    fun launchComplex() = runTest {
        var counter = 0
        withTimeout(50010) {
            coroutineScope {
                launch {
                    repeat(5) {
                        delay(4000L)
                        counter++
                    }
                }
                assertEquals(0, counter)
            }
            assertEquals(5, counter)

            coroutineScope {
                withTimeout(10010L) {
                    repeat(2) {
                        delay(5000L)
                        counter++
                    }
                }
                assertEquals(7, counter)
                launch {
                    repeat(5) {
                        delay(4000L)
                        counter++
                    }
                }
            }
            assertEquals(12, counter)
        }

        assertEquals(12, counter)
    }

    @Test
    fun test() = runTest {
        withContext(Dispatchers.Default) {
            withTimeout(1000) { // immediately times out
                apiCall()
            }
        }
    }

    suspend fun apiCall() = withContext(Dispatchers.IO) {
        delay(900) // not even 1s
    }

    @Test
    fun jobTest() = runTest(dispatchTimeoutMs = 1000L) {
        withContext(context = Dispatchers.Default) {
            var counter = 0
            val job = launch(start = CoroutineStart.LAZY) {
                delay(990L)
                counter++
                5
            }
            job.start() //
            assertEquals(0, counter)
            job.join() // job.start also executed automatically
            assertEquals(1, counter)
        }
    }

    @Test
    fun asyncTest() = runTest(dispatchTimeoutMs = 6000L) {
        withContext(context = Dispatchers.Default) {
            val r = async(context = Dispatchers.Default) {
                delay(2500)
                1
            }
            assertFalse { r.isCompleted }
            delay(1000)
            assertFalse { r.isCompleted }
            assertFailsWith<IllegalStateException> {
                assertEquals(1, r.getCompleted())
            }
            delay(2000)
            assertTrue { r.isCompleted }
            assertEquals(1, r.getCompleted())

            var flag = 0
            Assertions.assertThat(
                async(start = CoroutineStart.LAZY, context = Dispatchers.IO) {
                    delay(2500)
                    flag++
                    11
                }.await()
            ).isEqualTo(11)
            assertEquals(1, flag)
        }
    }

    @Test
    fun dispatcher() = runTest {
        assertTrue(Thread.currentThread().toString().contains("main"))
    }

    @Test
    fun cancelAndJoin() = runTest(dispatchTimeoutMs = 100L) {
        withContext(context = Dispatchers.Default) {
            delay(50)
            launch(context = Dispatchers.Default) {
                assertFailsWith<CancellationException> {
                    delay(2500)
                    1
                }
            }.cancelAndJoin()

            async(context = Dispatchers.Default) {
                assertFailsWith<CancellationException> {
                    delay(2500)
                    1
                }
            }.cancelAndJoin()

            val r = async(context = Dispatchers.Default) {
                delay(2500)
                1
            }
            r.cancelAndJoin()
            assertFailsWith<IllegalStateException> {
                assertEquals(1, r.getCompleted())
            }
        }
    }

    @Test
    fun channel() = runTest {
        withTimeout(3100) {
            val channel = Channel<String>()
            launch {
                for (i in 1..3) {
                    channel.send("message $i")
                    delay(1000)
                }
                channel.close()
            }

            launch {
                var i = 1
                for (m in channel) {
                    assertEquals("message ${i++}", m)
                }
            }
        }
    }

    fun CoroutineScope.getUsers(): ReceiveChannel<String> = produce {
        val users = listOf("Tom", "Bob", "Sam")
        for (user in users) {
            delay(50 + Random.nextLong(50))
            send(user)
        }
    }

    @Test
    fun produceConsume() = runTest {
        val users = mutableListOf<String>()
        getUsers().consumeEach { users.add(it) }

        assertEquals(listOf("Tom", "Bob", "Sam"), users)
    }
}
