package renovation.temp.controller

import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.math.pow
import kotlin.random.Random
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import renovation.temp.data.service.StreamService

@RestController
@RequestMapping("/stream")
class StreamController(
    private val serv: StreamService,
) {
    private fun s() = serv.data()

    @GetMapping("all")
    fun all() = serv.data()

    @GetMapping("map")
    fun map() = s().stream().map { e -> e.id }.distinct()

    @GetMapping("map2")
    fun map2() = s().stream().map { e -> e.id }.collect(Collectors.toSet())

    @GetMapping("maxOfFilteredIds")
    fun maxOfFilteredIds() =
        s().stream().filter { e -> e.name?.startsWith("s") == true }.mapToInt { e -> e.id.toInt() }.max().orElse(
            Int.MIN_VALUE
        )

    @GetMapping("sumOfFilteredIds")
    fun sumOfFilteredIds() =
        s().stream().filter { e -> e.name?.startsWith("s") == true }.mapToInt { e -> e.id.toInt() }
            .reduce(0, Integer::sum)
//    s().stream().filter { e -> e.name?.startsWith("s") == true }.mapToInt { e -> e.id.toInt() }.sum()

    @GetMapping("streamGenerator")
    fun streamGenerator() = Stream.generate { Random.nextInt(5) }.limit(5)
}

fun main() {
    val d = StreamService().data()

//    d.sortedBy { e -> e.id }

    val r = d.stream().filter { e -> e.name?.startsWith("s") == true }
//    println(r.count())
//    println(r.mapToInt { e->e.id.toInt() }.max().orElse(Int.MIN_VALUE))
//    println(r.mapToInt { e -> e.id.toInt() }.reduce(0, Integer::sum))

    val k: String? = null
    println(k?.startsWith("a"))
    println(k?.startsWith("a") == true)

    val l = { i: Int -> i.toString().reversed() }
    println(l.invoke(511222))

    println(cat(15) { n -> n.toString() })


}

fun cat(i: Int, k: (Int) -> String) {
    println("$$$ ${k.invoke(i.toDouble().pow(3.0).toInt())} $$$")
}
