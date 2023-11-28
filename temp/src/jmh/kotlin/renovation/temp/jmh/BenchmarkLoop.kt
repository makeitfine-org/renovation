/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.jmh

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Threads
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Threads(8)
@Warmup(iterations = 2) // Warmup Iteration = 3
@Measurement(iterations = 2) // Iteration = 8
@Fork(value = 2, jvmArgs = ["-Xms2G", "-Xmx2G"], warmups = 1)
open class BenchmarkLoop {

    @Param("2000000")
    private var number = 0

    private var dataForTest: List<String> = listOf()

    @Setup
    fun setup() {
        dataForTest = createData()
    }

    @Benchmark
    fun loopFor(bh: Blackhole) {
        for (i in dataForTest.indices) {
            val s: String = dataForTest[i] // take out n consume, fair with foreach
            bh.consume(s)
        }
    }

    @Benchmark
    fun loopWhile(bh: Blackhole) {
        var i = 0
        while (i < dataForTest.size) {
            val s: String = dataForTest.get(i)
            bh.consume(s)
            i++
        }
    }

    @Benchmark
    fun loopForEach(bh: Blackhole) {
        for (s in dataForTest) {
            bh.consume(s)
        }
    }

    @Benchmark
    fun loopIterator(bh: Blackhole) {
        val iterator: Iterator<String> = dataForTest.iterator()
        while (iterator.hasNext()) {
            val s = iterator.next()
            bh.consume(s)
        }
    }

    private fun createData(): List<String> {
        val data: MutableList<String> = ArrayList()
        for (i in 0..number) {
            data.add("Number : $i")
        }
        return data
    }
}
