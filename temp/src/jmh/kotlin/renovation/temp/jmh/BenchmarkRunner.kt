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
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole


@Fork(value = 1, warmups = 1)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class BenchmarkRunner {

    @Benchmark
    fun init1000(blackhole: Blackhole) {
        var sum = 0L
        for (i in 1..1_000) {
            sum += i
        }
//        println(sum)
        blackhole.consume(sum)
    }

    @Benchmark
    fun init1000000(blackhole: Blackhole) {
        var sum = 0L
        for (i in 1..1_000_000) {
            sum += i
        }
//        println(sum)
        blackhole.consume(sum)
    }

    @Benchmark
    fun inlineUse(blackhole: Blackhole) {
        blackhole.consume(1)
    }
}
