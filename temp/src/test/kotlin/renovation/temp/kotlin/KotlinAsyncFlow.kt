/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.kotlin

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class KotlinAsyncFlow {

    @Test
    fun flowDelayReal() = runTest(timeout = 3200L.milliseconds) {
        withContext(context = Dispatchers.Default) { // In such case real delay don't turn into virtual
            val users = mutableListOf<String>()
            getUsers(950).collect {
                println(it)
                users.add(it)
            }

            assertEquals(listOf("Tom", "Bob", "Sam"), users)
        }
    }

    @Test
    fun flowDelayVirtual() = runTest {
        withTimeout(30000) { // In such case real delay don't turn into virtual
            val users = mutableListOf<String>()
            getUsers(9900).collect {
                println(it)
                users.add(it)
            }

            assertEquals(listOf("Tom", "Bob", "Sam"), users)
        }
    }

    @Test
    fun flowDelayVirtualTimeout() = runTest {
        assertFailsWith<TimeoutCancellationException> {
            withTimeout(30000) { // In such case real delay don't turn into virtual
                val users = mutableListOf<String>()
                getUsers(10100).collect {
                    println(it)
                    users.add(it)
                }
            }
        }
    }

    private fun getUsers(delay: Long = 100): Flow<String> = flow {
        val database = listOf("Tom", "Bob", "Sam")
        for (item in database) {
            delay(delay)
            emit(item)
        }
    }

    @Test
    fun flowOfAnsAsFlowTest() = runTest {
        withTimeout(30000) { // In such case real delay don't turn into virtual
            val nums = mutableListOf<Int>()
            flowOf(1, 3, 5).collect { nums.add(it) }
            assertEquals(listOf(1, 3, 5), nums)

            assertEquals(
                135,
                flowOf(1, 3, 5)
                    .map { it.toString() }
                    .reduce { acc, value -> acc + value }
                    .toInt()
            )
            assertEquals(
                9,
                listOf(1, 3, 5)
                    .asFlow()
                    .reduce { acc, value -> acc + value }
                    .toInt()
            )
            assertEquals(
                10,
                listOf(1, 3, 5)
                    .asFlow()
                    .fold(1) { acc, value -> acc + value }
                    .toInt()
            )
            assertEquals(
                57,
                flowOf(1, 3, 5, 7)
                    .drop(2)
                    .map { it.toString() }
                    .reduce { acc, value -> acc + value }
                    .toInt()
            )
            assertEquals(
                "3!5!7!",
                flowOf(1, 3, 5, 7)
                    .transform { emit("$it!") }
                    .drop(1)
                    .reduce { acc, value -> acc + value }
            )
            assertEquals(
                "3!5!7!",
                flowOf(1, 3, 5, 7)
                    .transform { emit("$it!") }
                    .drop(1)
                    .reduce { acc, value -> acc + value }
            )
            assertEquals(
                3,
                flowOf(1, 3, 5, 7)
                    .transform { emit("$it!") }
                    .drop(1)
                    .count()
            )
            assertNotNull(flowOf(1).single())
            assertNull(flowOf(1).drop(1).singleOrNull())
        }
    }
}
