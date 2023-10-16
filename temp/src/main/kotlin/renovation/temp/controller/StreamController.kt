package renovation.temp.controller

import java.util.LinkedList
import java.util.UUID
import java.util.function.BiFunction
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.LongStream
import java.util.stream.Stream
import kotlin.math.pow
import kotlin.random.Random
import kotlin.streams.asSequence
import kotlin.streams.toList
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import renovation.temp.data.service.Rec
import renovation.temp.data.service.StreamService

private val log = KotlinLogging.logger { }

@RestController
@RequestMapping("/stream")
@Suppress("MagicNumber", "TooManyFunctions")
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
    //optional:    s().stream().filter { e -> e.name?.startsWith("s") == true }.mapToInt { e -> e.id.toInt() }.sum()

    @GetMapping("streamGenerator")
    fun streamGenerator(
        @RequestParam(defaultValue = "5") rand: Int = 5,
        @RequestParam(defaultValue = "5") limit: Long = 5
    ) = Stream.generate { Random.nextInt(rand) }.limit(limit)

    @GetMapping("sequenceGenerator")
    fun sequentGenerator(
        @RequestParam(defaultValue = "5") start: Int = 5,
        @RequestParam(defaultValue = "5") limit: Long = 5
    ) = Stream.iterate(start) { i -> i + 1 }.limit(limit)

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
            { m1, _ -> m1 })
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
}

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
}

@Suppress("MagicNumber", "UnusedPrivateMember")
fun main1() {
    val d = StreamService().data()

//    d.sortedBy { e -> e.id }

//    println(r.count())
//    println(r.mapToInt { e->e.id.toInt() }.max().orElse(Int.MIN_VALUE))
//    println(r.mapToInt { e -> e.id.toInt() }.reduce(0, Integer::sum))

    val k: String? = null
    println(k?.startsWith("a"))
    println(k?.startsWith("a") == true)

    val l: (Int) -> String = { i: Int -> i.toString().reversed() }
    println(l.invoke(511222))

    println(cat(15) { n -> n.toString() })

    val show = Show("d")
    val s = Stream.iterate(5) { i -> i + 2 }.limit(5).map { e -> show.elem(e) }

    s.forEach { println("$it ") }

    val fu: Function<Int, Double> = Function { i -> i.toDouble() }
    println(fu.apply(55))
//    val bifu: BiFunction<Int, Double, String> = BiFunction { i: Int, d: Double -> "$i $d" }
//    println(bifu.apply(5, 5.5))

    d.stream().map { e -> e.name }.reduce { s1, s2 -> "$s1 # $s2" }.get().let { println(it) }

    val dId = d.stream().map { it.id }.toList()

    val list = listOf(dId, dId, dId)

    list.stream().flatMap { it.stream() }.forEach { print("$it ") }
    println()
    list.stream().flatMap { it.stream().map { it.toString() } }.map(String::toDouble).toList().forEach(::print)

    val l2 = listOf(Show("d"), Show("31"), Show("e"), Show("key"), Show("ded"))

    l2.stream().filter { it.o.length > 1 }.filter { it.o.length == 2 }.collect(Collectors.toCollection(::ArrayList))
    l2.stream().filter { it.o.length > 1 }.filter { it.o.length == 2 }.collect(Collectors.toCollection(::LinkedList))
}

class Show {

    var o: String
        get() = field.also { println("$it ->") }
        set(value) {
            println("--- $value ---")
            field = value
        }


    fun elem(e: Int) = print("e: $e ").let { e }

    constructor(s: String) {
        o = s
        println("get $s")
    }

    override fun toString(): String {
        return "Show(o='$o')"
    }
}

@Suppress("MagicNumber")
fun cat(i: Int, k: (Int) -> String) {
    println("$$$ ${k.invoke(i.toDouble().pow(3.0).toInt())} $$$")
}
