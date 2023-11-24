package renovation.temp.controller

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.util.Arrays
import java.util.TreeMap
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Collectors
import java.util.stream.IntStream
import java.util.stream.LongStream
import java.util.stream.Stream
import kotlin.math.pow
import kotlin.random.Random
import kotlin.streams.toList
import mu.KotlinLogging
import org.apache.commons.lang3.stream.Streams
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import renovation.temp.data.service.Rec
import renovation.temp.data.service.StreamService


private val log = KotlinLogging.logger { }

typealias Guava = com.google.common.collect.Streams

@RestController
@RequestMapping("/stream")
@Suppress("detekt:all")
class StreamController(
    private val serv: StreamService,
) {
    private fun s() = serv.data().stream()

    @GetMapping("all")
    fun all() = serv.data().stream().peek(::println)

    @GetMapping("map")
    fun map() = s().map { e -> e.id }.distinct()

    @GetMapping("map2")
    fun map2() = s().map { e -> e.id }.collect(Collectors.toSet())

    @GetMapping("maxOfFilteredIds")
    fun maxOfFilteredIds() =
        s().filter { e -> e.name?.startsWith("s") == true }.mapToInt { e -> e.id.toInt() }.max().orElse(
            Int.MIN_VALUE
        )

    @GetMapping("sumOfFilteredIds")
    fun sumOfFilteredIds() =
        s().filter { e -> e.name?.startsWith("s") == true }.mapToInt { e -> e.id.toInt() }
            .reduce(0, Integer::sum).also { log.debug { it } }
    // optional:    s().stream().filter { e -> e.name?.startsWith("s") == true }.mapToInt { e -> e.id.toInt() }.sum()

    @GetMapping("streamGenerator")
    fun streamGenerator(
        @RequestParam(defaultValue = "5") rand: Int = 5,
        @RequestParam(defaultValue = "5") limit: Long = 5
    ) = Stream.generate { Random.nextInt(rand) }.limit(limit)

    @GetMapping("iterateGenerator")
    fun iterateGenerator(
        @RequestParam(defaultValue = "5") start: Int = 5,
        @RequestParam(defaultValue = "5") limit: Long = 5
    ) = Stream.iterate(start) { i -> i + 1 }.limit(limit)

    @GetMapping("iterateGenerator2")
    fun iterateGenerator2(
        @RequestParam(defaultValue = "5") start: Int = 5,
        @RequestParam(defaultValue = "5") limit: Long = 5
    ) = Stream.iterate(start, { it <= start * limit }) { it + 1 }


    @GetMapping("simpleFunc")
    fun simpleFunc() = ({ i: UUID -> i.toString() }).invoke(UUID.randomUUID())
    // val l: (UUID) -> String = { it.toString() }

    @GetMapping("func")
    fun func() = Function { i: Int -> i.toDouble() }.let { it.apply(Random.nextInt()) }
    // val fun1: Function<Int, Double> = Function { i -> i.toDouble() }

    @GetMapping("biFunc")
    fun biFunc() = BiFunction { i: Int, d: Double -> "$i ^ $d" }.let { it.apply(Random.nextInt(), 5.5) }
    // val biFunc: BiFunction<Int, Double, String> = BiFunction { i: Int, d: Double -> "$i ^ $d" }

    @GetMapping("reduceNames")
    fun reduceNames() = s().map { e -> e.name }.reduce { s1, s2 -> "$s1 #$s2" }.get()

    @GetMapping("flatMap")
    fun flatMap() =
        Stream.of(serv.data(), serv.data(), serv.data()).flatMap { it.stream() }.map(Rec::toString).toList()

    @GetMapping("prop")
    fun prop() = Stream.of(See("d"), See("31"), See("e"), See("key"), See("ded"), See("OK"))
        .filter { it.p.length > 1 }.filter { it.p.length == 2 }.toList()

    @GetMapping("sort1")
    fun sort1() = s().map { it.id }.sorted()

    @GetMapping("sort2")
    fun sort2() = s().sorted { e1, e2 -> e1.id.compareTo(e2.id) }

    @GetMapping("longStream")
    fun longStream() = s().mapToLong { it.id }

    @GetMapping("doubleStream")
    fun doubleStream() = s().mapToDouble { it.id.toDouble() + 0.5 }.toList()

    @GetMapping("average")
    fun average() = s().mapToDouble { it.id.toDouble() + 0.5 }.average()

    @GetMapping("range")
    fun range() = LongStream.range(5, 7)

    @GetMapping("reduce")
    fun reduce(): HashMap<Int, MutableMap<String, Int>> {
        val map: MutableMap<String, Int> = mutableMapOf();

        map["a"] = 1
        map["b"] = 1
        map["c"] = 10
        map["d"] = 1
        map["e"] = 100
        map["f"] = 10
        map["g"] = 1000

        return map.entries.stream().reduce(
            HashMap(),
            { m, e ->
                if (m[e.value] == null) {
                    val insideMap: MutableMap<String, Int> = HashMap();
                    insideMap[e.key] = e.value
                    m[e.value] = insideMap
                } else {
                    m[e.value]?.set(e.key, e.value)
                }
                m
            },
            { m1, _ -> m1 }
        )
    }

    @GetMapping("joining")
    fun joining() = s()
        .map { it.name?.chars() }
        .flatMapToInt { it }
        .mapToObj { "${it.toChar()}" }
        .collect(Collectors.joining(", ", ">>>", "<<<"))

    @GetMapping("summarizingIds")
    fun summarizingDouble() = s()
        .collect(Collectors.summarizingLong { it.id })

    @GetMapping("summaryStatisticsIds")
    fun summaryStatisticsIds() = s().mapToDouble() { it.id.toDouble() }.summaryStatistics()

    @GetMapping("groupingBy1")
    fun groupingBy1() = s().collect(Collectors.groupingBy { Pair(it.id, it.name) })

    @GetMapping("groupingBy2")
    fun groupingBy2() = s().collect(Collectors.groupingBy({ Pair(it.id, it.name) }, Collectors.counting()))

    @GetMapping("groupingBy3")
    fun groupingBy3() =
        s().collect(Collectors.groupingBy({ it.id }, Collectors.mapping({ it.name }, Collectors.toSet())))

    @GetMapping("groupingBy4")
    fun groupingBy4() = s().collect(
        Collectors.groupingBy(
            { it.id },
            ::TreeMap,
            Collectors.mapping(
                { Pair(it.name, it.name?.length) },
                Collectors.toSet()
            ),
        )
    )

    @GetMapping("groupingBy5")
    fun groupingBy5() =
        s().collect(
            Collectors.groupingBy(
                { it.id },
                Collectors.mapping({ it.name ?: "" }, Collectors.joining(", ", "{", "}"))
            )
        )

    @GetMapping("groupingBy6")
    fun groupingBy6() =
        s().collect(
            Collectors.groupingBy(
                { it.id },
                Collectors.groupingBy(
                    { it.name ?: "" },
                    Collectors.mapping({ it.any }, Collectors.toSet())
                )
            )
        )

    @GetMapping("groupingBy7")
    fun groupingBy7() =
        s().collect(
            Collectors.groupingBy(
                { (it.name ?: "").length },
                Collectors.mapping(
                    { "${it.id}: ${it.name ?: ""}" },
                    Collectors.toList()
                )
            )
        )

    @GetMapping("groupingBy8")
    fun groupingBy8() =
        s().filter { it.name != null }
            .collect(
                Collectors.groupingBy(
                    { (it.name ?: "").length },
                    Collectors.mapping(
                        { "${it.id}: ${it.name ?: ""}" },
                        Collectors.toList()
                    )
                )
            )

    @GetMapping("groupingByConcurrent8")
    fun groupingByConcurrent8() =
        s().filter { it.name != null }
            .collect(
                Collectors.groupingByConcurrent(
                    { (it.name ?: "").length },
                    Collectors.mapping(
                        { "${it.id}: ${it.name ?: ""}" },
                        Collectors.toList()
                    )
                )
            )

    @GetMapping("partitioningBy") // throws exception
    fun partitioningBy() = s()
        .map { it.id }
        .collect(
            Collectors.partitioningBy {
                it % 2 == 0L
            }
        )

    @GetMapping("partitioningBy1") // throws exception
    fun partitioningBy1() = s()
        .map { it.id }
        .collect(
            Collectors.partitioningBy(
                {
                    it % 2 == 0L
                },
                Collectors.toSet()
            )
        )

    @GetMapping("reducing")
    fun reducing() =
        s().collect(
            Collectors.reducing(0, { it.id })
            { s1, s2 -> s1 + s2 }
        )

    @GetMapping("reducing2")
    fun reducing2() =
        s().collect(
            Collectors.groupingBy(
                { it.id },
                Collectors.reducing(
                    0,
                    { it.id },
                    { s1, s2 -> s1 + s2 }
                )
            )
        )

    @GetMapping("nullable")
    fun nullable(@RequestParam(required = false) num: Int?): Int {
        val result = Stream.ofNullable(num)
        return result.map { it!! * it }.findFirst().orElse(Int.MIN_VALUE)
    }

    @GetMapping("exception1")
    fun exception1(): String {
        try {
            val some: Int? = null
            val stream = Stream.of(some)
            stream.map { it!! * 2 }.toList()
        } catch (e: RuntimeException) {
            return "Catch exception"
        }
        return "No exception"
    }

    @GetMapping("tryCatchStream")
    fun tryCatchStream() = Stream.of(Path.of("no-no")).map {
        try {
            Files.readString(it)
        } catch (e: IOException) {
            "no"
        }
    }.toList()

    @GetMapping("failableStream") // throws exception (for java suitable)
    fun failableStream() = Streams.failableStream(Stream.of(Path.of("no-no"))).map {
        Files.readString(it)
    }.collect(Collectors.toList())

    @GetMapping("failableStreamKotlin") // throws exception
    fun failableStreamKotlin() = Stream.of(Path.of("no-no")).map {
        Files.readString(it)
    }.collect(Collectors.toList())

    @GetMapping("index") // throws exception
    fun index() =
        Guava.mapWithIndex(
            s()
        ) { e, i ->
            if (i % 2L == 0L) {
                e
            } else {
                null
            }
        }
            .filter { it != null }
            .toList()

    @GetMapping("performance")
    fun performance(): String {
        val b = System.nanoTime();
        val l = (1..25_000_000)
            .filter { it % 2 == 1 }
            .map { it * 2 }
            .take(2)
        val e = System.nanoTime();

        log.debug { "$l >>> [begin:$b | end:$e | time millis: ${(e - b) / 1000000.0}]" }

        val b2 = System.nanoTime();
        val l2 = (1..1_925_000_000).asSequence()
            .filter { it % 2 == 1 }
            .map { it * 2 }
            .take(2)
        val e2 = System.nanoTime();

        log.debug { "${l2.toList()} >>> [begin:$b2 | end:$e2 | time millis: ${(e2 - b2) / 1000000.0}]" }

        val b3 = System.nanoTime();
        val l3 = Stream.iterate(1, { it < 1_925_000_000 }) { it + 1 }
            .filter { it % 2 == 1 }
            .map { it * 2 }
            .limit(2)
        val e3 = System.nanoTime();

        log.debug { "${l3.toList()} >>> [begin:$b3 | end:$e3 | time millis: ${(e3 - b3) / 1000000.0}]" }

        return "performance statistics in log"
    }

    @GetMapping("teeing")
    fun teeing() = s().collect(
        Collectors.teeing(
            Collectors.filtering({ it.id < 5 }, Collectors.toSet()),
            Collectors.filtering({ it.id > 5 }, Collectors.toSet())
        ) { c1, c2 -> setOf(c1, c2) }
    )

    @GetMapping("mapCollection")
    fun mapCollection() = s().distinct().collect(
        Collectors.toMap(
            { it },
            { it.name ?: "" }
        )
    )

    @GetMapping("distinctValue")
    fun distinctValue() = s().filter(distKey({ it.id }))
        .toList()
}

fun <T> distKey(vararg keys: Function<T, Any>): Predicate<T> {
    val seen = ConcurrentHashMap<List<Any>, Boolean>()

    return Predicate { t ->
        val nextKeys = Arrays.stream(keys)
            .map { it.apply(t) }
            .collect(Collectors.toList())
        println(nextKeys)
        seen.putIfAbsent(nextKeys, true) == null
    }
}

@Suppress("detekt:all")
class See {
    var p: String
        get() = field.also { log.debug("GET: $it") }
        set(value) {
            log.debug("SET: $value")
            field = value
        }

    constructor(pInit: String) {
        p = pInit
        log.debug("Costructor: $pInit")
    }

    override fun toString(): String {
        log.debug("toString")
        return "Show(p='$p')"
    }

    fun cat(i: Int, k: (Int) -> String) {
        println("$$$ ${k.invoke(i.toDouble().pow(3.0).toInt())} $$$")
    }
}

@Suppress("detekt:all")
fun mainTemp() {
    val d = StreamService().data()

    val b = System.nanoTime();
    val l = (1..25_000_000)
        .filter { it % 2 == 1 }
        .map { it * 2 }
        .take(2)
    val e = System.nanoTime();

    println("$l >>> [begin:$b | end:$e | time millis: ${(e - b) / 1000000.0}]")

    val l2 = (1..1_925_000_000).asSequence()
        .filter { it % 2 == 1 }
        .map { it * 2 }
        .take(2)
    println(l2)

    val e2 = System.nanoTime();
    println(e2)

    val b3 = System.nanoTime();
    val l3 = Stream.iterate(1, { it < 1_925_000_000 }) { it + 1 }
        .filter { it % 2 == 1 }
        .map { it * 2 }
        .limit(2)
    val e3 = System.nanoTime();

    println("${l3.toList()} >>> [begin:$b3 | end:$e3 | time millis: ${(e3 - b3) / 1000000.0}]")

    val isYes = Predicate { arg: Int -> arg > 5 }

    println(isYes.test(55))
    println(isYes.test(-234))

    d.stream().distinct().collect(
        Collectors.toMap(
            { it },
            { it.name ?: "" }
        )
    )

    println("---------")
    Stream.of(2, 333, 23, 23, -23).parallel().forEachOrdered { c ->
        print("a$c ")
    }

    println()
    println(" - - - - -- -  ")

    Stream.of("Monkey", 3, "Lion", "Giraffe", "Lemur", "Lion", 1).parallel()
//        .forEach(::print)
        .forEachOrdered(::print)

    println("888888888888888888")

    val list = IntStream.of(1, 2, 3, 4, 5).boxed()
        .collect(Collectors.toList())

    println(list)

    val today = LocalDate.now()

    val next3Days = today.datesUntil(today.plusDays(3))

    next3Days.forEach { x: LocalDate? -> println(x) }
}



