/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.kotlin

import java.io.IOException
import java.io.UncheckedIOException
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.util.TreeMap
import java.util.UUID
import java.util.function.BiFunction
import java.util.stream.Collectors
import java.util.stream.LongStream
import java.util.stream.Stream
import kotlin.random.Random
import kotlin.streams.toList
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.apache.commons.lang3.stream.Streams
import org.assertj.core.api.Assertions.assertThat
import renovation.common.util.Json.jsonAssertEquals

typealias Guava = com.google.common.collect.Streams

@Suppress("detekt:LargeClass")
class StreamsTest {

    companion object {
        data class Rec(
            val id: Long,
            val name: String? = null,
            val data: Any? = null,
        )

        @JvmStatic
        private val data = listOf(
            Rec(1, "first"),
            Rec(2, "second"),
            Rec(3),
            Rec(4, "Four four", listOf("a", 1, 3.3)),
            Rec(5, "name five", listOf("a", 21, 3.3)),
            Rec(6, "six", listOf("a", 1, 3.3)),
            Rec(6, "six", mapOf(Pair(5, "d"))),
            Rec(7),
            Rec(8, "eight one", 555.555),
            Rec(8, "eight one", 555.555),
            Rec(3, "same as 3333", "three"),
            Rec(4, "four", 444),
        )
    }

    private lateinit var dataStream: Stream<Rec>

    @BeforeTest
    fun init() {
        dataStream = data.stream()
    }

    @Test
    fun peek() {
        val actual = dataStream.peek(::println).toList()

        jsonAssertEquals(
            """
                [
                  {
                    "id": 1,
                    "name": "first",
                    "data": null
                  },
                  {
                    "id": 2,
                    "name": "second",
                    "data": null
                  },
                  {
                    "id": 3,
                    "name": null,
                    "data": null
                  },
                  {
                    "id": 4,
                    "name": "Four four",
                    "data": [
                      "a",
                      1,
                      3.3
                    ]
                  },
                  {
                    "id": 5,
                    "name": "name five",
                    "data": [
                      "a",
                      21,
                      3.3
                    ]
                  },
                  {
                    "id": 6,
                    "name": "six",
                    "data": [
                      "a",
                      1,
                      3.3
                    ]
                  },
                  {
                    "id": 6,
                    "name": "six",
                    "data": {
                      "5": "d"
                    }
                  },
                  {
                    "id": 7,
                    "name": null,
                    "data": null
                  },
                  {
                    "id": 8,
                    "name": "eight one",
                    "data": 555.555
                  },
                  {
                    "id": 8,
                    "name": "eight one",
                    "data": 555.555
                  },
                  {
                    "id": 3,
                    "name": "same as 3333",
                    "data": "three"
                  },
                  {
                    "id": 4,
                    "name": "four",
                    "data": 444
                  }
                ]
            """,
            actual
        )
    }

    @Test
    fun mapWithDistinct() {
        val actual = dataStream.map { e -> e.id }.distinct()

        jsonAssertEquals(
            """
           [
              1,
              2,
              3,
              4,
              5,
              6,
              7,
              8
            ]
            """,
            actual
        )
    }

    @Test
    fun mapWithList() {
        val actual = dataStream.map { e -> e.id }.toList()

        jsonAssertEquals(
            """
           [
              1,
              2,
              3,
              4,
              5,
              6,
              6,
              7,
              8,
              8,
              3,
              4
            ]
            """,
            actual
        )
    }

    @Test
    fun mapWithSet() {
        val actual = dataStream.map { e -> e.id }.distinct()

        jsonAssertEquals(
            """
            [
              1,
              2,
              3,
              4,
              5,
              6,
              7,
              8
            ]
            """,
            actual
        )
    }

    @Test
    fun maxOfFilteredIds() {
        val actual = dataStream
            .filter { e -> e.name?.startsWith("s") == true }
            .mapToInt { e -> e.id.toInt() }
            .max()
            .takeIf { !it.isEmpty }
            ?.asInt

        assertEquals(6, actual)
    }

    @Test
    fun maxOfFilteredIds_butNoIds() {
        val actual = dataStream
            .filter { e -> e.name?.startsWith("234o@!234go") == true }
            .mapToInt { e -> e.id.toInt() }
            .max()
            .takeIf { !it.isEmpty }
            ?.asInt

        assertNull(actual)
    }

    @Test
    fun sumOfFilteredIds() {
        val actual = dataStream
            .filter { e -> e.name?.startsWith("s") == true }
            .mapToInt { e -> e.id.toInt() }
            .reduce(0, Integer::sum).also { println(it) }

        assertEquals(17, actual)
    }

    @Test
    fun streamGenerator() {
        val actual = Stream.generate { Random.nextInt(3) }.limit(5)

        val list = actual.toList()
        assertTrue { list.min() >= 0 }
        assertTrue { list.max() <= 4 }
        assertEquals(5, list.size)
    }

    @Test
    fun iterateGenerator() {
        val actual = Stream.iterate(3) { i -> i + 2 }.limit(5)

        val list = actual.toList()
        assertThat(list == listOf(3, 5, 7, 9, 11)).isTrue()
    }

    @Test
    fun iterateGeneratorByCondition() {
        val actual = Stream.iterate(3, { it <= 10 }) { it + 3 }

        val list = actual.toList()
        assertThat(list == listOf(3, 6, 9)).isTrue()
    }

    @Test
    fun lambdaInvoke() {
        // same as: UUID.randomUUID().toString()
        val actual = ({ i: UUID -> i.toString() }).invoke(UUID.randomUUID())

        assertThat(actual.length)
        assertThat(
            actual.matches(
                Regex(
                    "[a-f0-9]{8}+-[a-f0-9]{4}+-[a-f0-9]{4}+-[a-f0-9]{4}+-[a-f0-9]{12}+"
                )
            )
        ).isTrue()
    }

    @Test
    fun javaFunction() {
        val actual = java.util.function.Function { i: Int -> i.toDouble() }
            .let { it.apply(Random.nextInt(100) - 50) }

        assertThat(actual).isGreaterThanOrEqualTo(-50.0)
        assertThat(actual).isLessThanOrEqualTo(50.0)
    }

    @Test
    fun kotlinFunction() {
        val actual = { i: Int -> i.toDouble() }
            .let { it.invoke(Random.nextInt(100) - 50) } // same as { i: Int -> i.toDouble() }(Random.nextInt(100) - 50)

        assertThat(actual).isGreaterThanOrEqualTo(-50.0)
        assertThat(actual).isLessThanOrEqualTo(50.0)
    }

    @Test
    fun kotlinSupplier() {
        val supplier = { "sum value" } // same as { -> "sum value" }

        assertEquals("sum value", supplier())
    }

    @Test
    fun kotlinConsumer() {
        val consumer = { sb: StringBuilder ->
            sb.append("sum value")
            Unit
        }

        val sb = StringBuilder("init ")
        consumer(sb)

        assertEquals("init sum value", sb.toString())
    }

    @Test
    fun javaBiFunction() {
        val actual = BiFunction { i: Int, d: Double -> "$i ^ $d" }
            .let { it.apply(75, 5.5) }

        assertEquals("75 ^ 5.5", actual)
    }

    @Test
    fun reduceNames() {
        val actual = dataStream
            .map { e -> e.name }
            .filter { !it.isNullOrBlank() }
            .reduce { s1, s2 -> "$s1 #$s2" }
            .get()

        assertEquals("first #second #Four four #name five #six #six #eight one #eight one #same as 3333 #four", actual)
    }

    @Test
    fun flatMap() {
        val actual = Stream.of(data, data, data)
            .flatMap { it.stream() }
            .map(Rec::toString)
            .collect(Collectors.toSet())

        assertEquals(data.asSequence().map { e -> e.toString() }.toSet(), actual)
        jsonAssertEquals(
            """
            [
              "Rec(id=8, name=eight one, data=555.555)",
              "Rec(id=4, name=four, data=444)",
              "Rec(id=3, name=null, data=null)",
              "Rec(id=1, name=first, data=null)",
              "Rec(id=7, name=null, data=null)",
              "Rec(id=3, name=same as 3333, data=three)",
              "Rec(id=5, name=name five, data=[a, 21, 3.3])",
              "Rec(id=6, name=six, data=[a, 1, 3.3])",
              "Rec(id=2, name=second, data=null)",
              "Rec(id=6, name=six, data={5=d})",
              "Rec(id=4, name=Four four, data=[a, 1, 3.3])"
            ]
            """,
            actual
        )
    }

    @Test
    fun properties() {
        class Tempo {
            var p: String = ""
                get() = field.let { "<$it>" }
                set(value) {
                    field = "!$value"
                }
        }

        val temp = Tempo()

        with(temp) {
            assertEquals("<>", p)
            p = "s"
            assertEquals("<!s>", this.p)
        }
    }

    @Test
    fun sortedIds() {
        val actual = dataStream.map { it.id }.sorted()

        jsonAssertEquals(
            """
            [
              1,
              2,
              3,
              3,
              4,
              4,
              5,
              6,
              6,
              7,
              8,
              8
            ]
            """,
            actual
        )
    }

    @Test
    fun sortById() {
        val actual = dataStream.sorted { e1, e2 -> e1.id.compareTo(e2.id) }.toList()

        assertEquals(data.sortedBy { it.id }, actual)

        jsonAssertEquals(
            """
            [
              {
                "id": 1,
                "name": "first",
                "data": null
              },
              {
                "id": 2,
                "name": "second",
                "data": null
              },
              {
                "id": 3,
                "name": null,
                "data": null
              },
              {
                "id": 3,
                "name": "same as 3333",
                "data": "three"
              },
              {
                "id": 4,
                "name": "Four four",
                "data": [
                  "a",
                  1,
                  3.3
                ]
              },
              {
                "id": 4,
                "name": "four",
                "data": 444
              },
              {
                "id": 5,
                "name": "name five",
                "data": [
                  "a",
                  21,
                  3.3
                ]
              },
              {
                "id": 6,
                "name": "six",
                "data": [
                  "a",
                  1,
                  3.3
                ]
              },
              {
                "id": 6,
                "name": "six",
                "data": {
                  "5": "d"
                }
              },
              {
                "id": 7,
                "name": null,
                "data": null
              },
              {
                "id": 8,
                "name": "eight one",
                "data": 555.555
              },
              {
                "id": 8,
                "name": "eight one",
                "data": 555.555
              }
            ]
            """,
            actual
        )
    }

    @Test
    fun mapToLongStream() {
        val actual = dataStream.mapToLong { it.id }.toList()

        jsonAssertEquals(
            """
            [
              1,
              2,
              3,
              4,
              5,
              6,
              6,
              7,
              8,
              8,
              3,
              4
            ]
            """,
            actual
        )
    }

    @Test
    fun mapToModificatedDoubleStream() {
        val actual = dataStream.mapToDouble { it.id.toDouble() + 0.5 }.toList()

        jsonAssertEquals(
            """
            [
              1.5,
              2.5,
              3.5,
              4.5,
              5.5,
              6.5,
              6.5,
              7.5,
              8.5,
              8.5,
              3.5,
              4.5
            ]
            """,
            actual
        )
    }

    @Test
    fun averageValueOfDoubleStream() {
        val actual = dataStream.mapToDouble { it.id.toDouble() + 0.5 }.average().asDouble

        assertEquals(5.25, actual)
    }

    @Test
    fun range() {
        val actual = LongStream.range(5, 8).toList()

        jsonAssertEquals(
            """
            [
              5,
              6,
              7
            ]
            """,
            actual
        )
    }

    @Test
    fun hashMapReduce() {
        val map: MutableMap<String, Int> = mutableMapOf()

        map["a"] = 1
        map["b"] = 1
        map["c"] = 10
        map["d"] = 1
        map["e"] = 100
        map["f"] = 10
        map["g"] = 1000

        val actual: HashMap<Int, MutableList<String>> = map.entries.stream().reduce(
            HashMap(),
            { m, e ->
                if (m[e.value] == null) {
                    val insideList: MutableList<String> = mutableListOf(e.key)
                    m[e.value] = insideList
                } else {
                    m[e.value]?.add(e.key)
                }
                m
            },
            { m1, _ -> m1 }
        )

        assertEquals(
            map.entries.stream().collect(
                Collectors.groupingBy(
                    { it.value },
                    Collectors.mapping({ it.key }, Collectors.toList())
                )
            ),
            actual
        )

        jsonAssertEquals(
            """
            {
              "1": [
                "a",
                "b",
                "d"
              ],
              "100": [
                "e"
              ],
              "1000": [
                "g"
              ],
              "10": [
                "c",
                "f"
              ]
            }
        """,
            actual
        )
    }

    @Test
    fun joining() {
        val actual = dataStream.map { it.name?.chars() }
            .flatMapToInt { it }
            .mapToObj { "${it.toChar()}" }
            .collect(Collectors.joining(", ", ">>>", "<<<"))

        assertEquals(
            ">>>f, i, r, s, t, s, e, c, o, n, d, F, o, u, r,  , " +
                "f, o, u, r, n, a, m, e,  , f, i, v, e, s, i, x, " +
                "s, i, x, e, i, g, h, t,  , o, n, e, e, i, g, h, t,  , " +
                "o, n, e, s, a, m, e,  , a, s,  , 3, 3, 3, 3, f, o, u, r<<<",
            actual
        )
    }

    @Test
    fun summarizingDouble() {
        val actual = dataStream.collect(Collectors.summarizingLong { it.id })

        jsonAssertEquals(
            """
            {
              "count": 12,
              "sum": 57,
              "min": 1,
              "max": 8,
              "average": 4.75
            }
            """.trimIndent(),
            actual
        )
    }

    @Test
    fun summaryStatisticsIds() {
        val actual = dataStream.mapToDouble { it.id.toDouble() }.summaryStatistics()

        jsonAssertEquals(
            """
            {
              "count": 12,
              "sum": 57.0,
              "min": 1.0,
              "max": 8.0,
              "average": 4.75
            }
            """.trimIndent(),
            actual
        )
    }

    @Test
    fun groupByPairOfIdAndName() {
        val actual = dataStream.collect(
            Collectors.groupingBy { Pair(it.id, it.name) }
        )

        jsonAssertEquals(
            """
            {
              "(4, Four four)": [
                {
                  "id": 4,
                  "name": "Four four",
                  "data": [
                    "a",
                    1,
                    3.3
                  ]
                }
              ],
              "(1, first)": [
                {
                  "id": 1,
                  "name": "first",
                  "data": null
                }
              ],
              "(4, four)": [
                {
                  "id": 4,
                  "name": "four",
                  "data": 444
                }
              ],
              "(3, same as 3333)": [
                {
                  "id": 3,
                  "name": "same as 3333",
                  "data": "three"
                }
              ],
              "(7, null)": [
                {
                  "id": 7,
                  "name": null,
                  "data": null
                }
              ],
              "(2, second)": [
                {
                  "id": 2,
                  "name": "second",
                  "data": null
                }
              ],
              "(8, eight one)": [
                {
                  "id": 8,
                  "name": "eight one",
                  "data": 555.555
                },
                {
                  "id": 8,
                  "name": "eight one",
                  "data": 555.555
                }
              ],
              "(6, six)": [
                {
                  "id": 6,
                  "name": "six",
                  "data": [
                    "a",
                    1,
                    3.3
                  ]
                },
                {
                  "id": 6,
                  "name": "six",
                  "data": {
                    "5": "d"
                  }
                }
              ],
              "(3, null)": [
                {
                  "id": 3,
                  "name": null,
                  "data": null
                }
              ],
              "(5, name five)": [
                {
                  "id": 5,
                  "name": "name five",
                  "data": [
                    "a",
                    21,
                    3.3
                  ]
                }
              ]
            }
            """.trimIndent(),
            actual
        )
    }

    @Test
    fun groupByPairOfIdAndNameGetCounting() {
        val actual = dataStream.collect(
            Collectors.groupingBy(
                { Pair(it.id, it.name) },
                Collectors.counting()
            )
        )

        jsonAssertEquals(
            """
            {
              "(4, Four four)": 1,
              "(1, first)": 1,
              "(4, four)": 1,
              "(3, same as 3333)": 1,
              "(7, null)": 1,
              "(2, second)": 1,
              "(8, eight one)": 2,
              "(6, six)": 2,
              "(3, null)": 1,
              "(5, name five)": 1
            }
            """,
            actual
        )
    }

    @Test
    fun groupByPairOfIdAndNameGetMapByNameCollectToSet() {
        val actual = dataStream.collect(
            Collectors.groupingBy(
                { it.id },
                Collectors.mapping({ it.name }, Collectors.toSet())
            )
        )

        jsonAssertEquals(
            """
            {
              "1": [
                "first"
              ],
              "2": [
                "second"
              ],
              "3": [
                null,
                "same as 3333"
              ],
              "4": [
                "four",
                "Four four"
              ],
              "5": [
                "name five"
              ],
              "6": [
                "six"
              ],
              "7": [
                null
              ],
              "8": [
                "eight one"
              ]
            }
            """,
            actual
        )
    }

    @Test
    fun groupByPairOfIdAndNameGetMapByNameCollectByJoining() {
        val actual = dataStream.collect(
            Collectors.groupingBy(
                { it.id },
                Collectors.mapping({ it.name ?: "" }, Collectors.joining(", ", "{", "}"))
            )
        )

        jsonAssertEquals(
            """
            {
              "1": "{first}",
              "2": "{second}",
              "3": "{, same as 3333}",
              "4": "{Four four, four}",
              "5": "{name five}",
              "6": "{six, six}",
              "7": "{}",
              "8": "{eight one, eight one}"
            }
            """,
            actual
        )
    }

    @Test
    fun groupByPairOfNameAndNameLengthGetBySet() {
        val actual = dataStream.collect(
            Collectors.groupingBy(
                { it.id },
                ::TreeMap,
                Collectors.mapping(
                    { Pair(it.name, it.name?.length) },
                    Collectors.toSet()
                ),
            )
        )

        jsonAssertEquals(
            """
            {
              "1": [
                {
                  "first": "first",
                  "second": 5
                }
              ],
              "2": [
                {
                  "first": "second",
                  "second": 6
                }
              ],
              "3": [
                {
                  "first": null,
                  "second": null
                },
                {
                  "first": "same as 3333",
                  "second": 12
                }
              ],
              "4": [
                {
                  "first": "Four four",
                  "second": 9
                },
                {
                  "first": "four",
                  "second": 4
                }
              ],
              "5": [
                {
                  "first": "name five",
                  "second": 9
                }
              ],
              "6": [
                {
                  "first": "six",
                  "second": 3
                }
              ],
              "7": [
                {
                  "first": null,
                  "second": null
                }
              ],
              "8": [
                {
                  "first": "eight one",
                  "second": 9
                }
              ]
            }
            """,
            actual
        )
    }

    @Test
    fun groupByNameLengthAndMappingByIdToNameCollectByList() {
        val actual = dataStream.collect(
            Collectors.groupingBy(
                { (it.name ?: "").length },
                Collectors.mapping(
                    { "${it.id}: ${it.name ?: ""}" },
                    Collectors.toList()
                )
            )
        )

        jsonAssertEquals(
            """
            {
              "0": [
                "3: ",
                "7: "
              ],
              "3": [
                "6: six",
                "6: six"
              ],
              "4": [
                "4: four"
              ],
              "5": [
                "1: first"
              ],
              "6": [
                "2: second"
              ],
              "9": [
                "4: Four four",
                "5: name five",
                "8: eight one",
                "8: eight one"
              ],
              "12": [
                "3: same as 3333"
              ]
            }
            """,
            actual
        )
    }

    @Test
    fun filterNameNotNullGroupByNameLengthAndMappingByIdToNameCollectByList() {
        val actual = dataStream
            .filter { it.name != null }
            .collect(
                Collectors.groupingBy(
                    { it.name!!.length },
                    Collectors.mapping(
                        { "${it.id}: ${it.name ?: ""}" },
                        Collectors.toSet()
                    )
                )
            )

        jsonAssertEquals(
            """
            {
              "3": [
                "6: six"
              ],
              "4": [
                "4: four"
              ],
              "5": [
                "1: first"
              ],
              "6": [
                "2: second"
              ],
              "9": [
                "8: eight one",
                "4: Four four",
                "5: name five"
              ],
              "12": [
                "3: same as 3333"
              ]
            }
            """,
            actual
        )
    }

    @Test
    fun filterNameNotNullGroupByConcurrentNameLengthAndMappingByIdToNameCollectByList() {
        val actual = dataStream
            .filter { it.name != null }
            .collect(
                Collectors.groupingByConcurrent(
                    { (it.name ?: "").length },
                    Collectors.mapping(
                        { "${it.id}: ${it.name ?: ""}" },
                        Collectors.toList()
                    )
                )
            )

        jsonAssertEquals(
            """
            {
              "3": [
                "6: six",
                "6: six"
              ],
              "4": [
                "4: four"
              ],
              "5": [
                "1: first"
              ],
              "6": [
                "2: second"
              ],
              "9": [
                "4: Four four",
                "5: name five",
                "8: eight one",
                "8: eight one"
              ],
              "12": [
                "3: same as 3333"
              ]
            }
            """,
            actual
        )
    }

    @Test
    fun partitioningById() {
        val actual = dataStream
            .map { it.id }
            .collect(
                Collectors.partitioningBy {
                    it % 2 == 0L
                }
            )

        jsonAssertEquals(
            """
           {
              "false": [
                1,
                3,
                5,
                7,
                3
              ],
              "true": [
                2,
                4,
                6,
                6,
                8,
                8,
                4
              ]
            }
            """,
            actual
        )
    }

    @Test
    fun partitioningByIdToSet() {
        val actual = dataStream
            .map { it.id }
            .collect(
                Collectors.partitioningBy(
                    {
                        it % 2 == 0L
                    },
                    Collectors.toSet()
                )
            )

        jsonAssertEquals(
            """
           {
              "false": [
                1,
                3,
                5,
                7
              ],
              "true": [
                2,
                4,
                6,
                8
              ]
            }
            """,
            actual
        )
    }

    @Test
    fun reducingByIdSum() {
        val actual = dataStream.collect(
            Collectors.reducing(
                0,
                { it.id }
            ) { s1, s2 -> s1 + s2 }
        )

        assertEquals(57, actual)
    }

    @Test
    fun groupingByIdAndReduceById() {
        val actual = dataStream.collect(
            Collectors.groupingBy(
                { it.id },
                Collectors.reducing(
                    0,
                    { it.id },
                    { s1, s2 -> s1 + s2 }
                )
            )
        )

        jsonAssertEquals(
            """
            {
              "1": 1,
              "2": 2,
              "3": 6,
              "4": 8,
              "5": 5,
              "6": 12,
              "7": 7,
              "8": 16
            }
            """,
            actual
        )
    }

    @Test
    fun nullable() {
        val actual = Stream.ofNullable(55)
            .map { it!! * it }
            .findFirst()
            .orElse(Int.MIN_VALUE)

        assertEquals(3025, actual)
    }

    @Test
    fun nullPointerExceptionFromStream() {
        val e = assertFailsWith<NullPointerException> {
            val some: Int? = null
            val stream = Stream.of(some)
            stream.map { it!! * 2 }.toList()
        }

        assertNull(e.message)
    }

    @Test
    fun ioExceptionFromStream() {
        Stream.of(Path.of("no-no")).map {
            val e = assertFailsWith<IOException> {
                Files.readString(it)
            }

            assertEquals("no-no", e.message)
        }.toList()
    }

    @Test
    @Suppress("detekt:SwallowedException")
    fun ioExceptionFromStreamOtherVariant() {
        val actual = Stream.of(Path.of("no-no")).map {
            try {
                Files.readString(it)
            } catch (e: IOException) {
                "no"
            }
        }.toList()

        assertEquals(1, actual.size)
        assertEquals("no", actual[0])
    }

    @Test
    fun rangeAndTake() {
        val actual = (1..25_000_000)
            .filter { it % 2 == 1 }
            .map { it * 2 }
            .take(2)

        assertThat(actual == listOf(2, 6)).isTrue()
    }

    @Test
    fun failableStream() {
        val e = assertFailsWith<UncheckedIOException> {
            Streams.failableStream(Stream.of(Path.of("no-no"))).map {
                Files.readString(it)
            }.collect(Collectors.toList())
        }

        assertEquals("java.nio.file.NoSuchFileException: no-no", e.message)
    }

    @Test
    fun failableStreamKotlin() {
        val e = assertFailsWith<NoSuchFileException> {
            Stream.of(Path.of("no-no")).map {
                Files.readString(it)
            }.collect(Collectors.toList())
        }

        assertEquals("no-no", e.message)
    }

    @Test
    fun guavaMapWithIndex() {
        val actual = Guava.mapWithIndex(
            dataStream
        ) { e, i ->
            if (i % 2L == 0L) {
                e
            } else {
                null
            }
        }
            .filter { it != null }
            .toList()

        jsonAssertEquals(
            """
            [
              {
                "id": 1,
                "name": "first",
                "data": null
              },
              {
                "id": 3,
                "name": null,
                "data": null
              },
              {
                "id": 5,
                "name": "name five",
                "data": [
                  "a",
                  21,
                  3.3
                ]
              },
              {
                "id": 6,
                "name": "six",
                "data": {
                  "5": "d"
                }
              },
              {
                "id": 8,
                "name": "eight one",
                "data": 555.555
              },
              {
                "id": 3,
                "name": "same as 3333",
                "data": "three"
              }
            ]
            """,
            actual
        )
    }

    @Test
    fun teeing() {
        val actual = dataStream.collect(
            Collectors.teeing(
                Collectors.filtering({ it.id < 5 }, Collectors.toSet()),
                Collectors.filtering({ it.id > 5 }, Collectors.toSet())
            ) { c1, c2 -> setOf(c1, c2) }
        )

        jsonAssertEquals(
            """
            [
              [
                {
                  "id": 2,
                  "name": "second",
                  "data": null
                },
                {
                  "id": 3,
                  "name": null,
                  "data": null
                },
                {
                  "id": 4,
                  "name": "Four four",
                  "data": [
                    "a",
                    1,
                    3.3
                  ]
                },
                {
                  "id": 1,
                  "name": "first",
                  "data": null
                },
                {
                  "id": 3,
                  "name": "same as 3333",
                  "data": "three"
                },
                {
                  "id": 4,
                  "name": "four",
                  "data": 444
                }
              ],
              [
                {
                  "id": 6,
                  "name": "six",
                  "data": [
                    "a",
                    1,
                    3.3
                  ]
                },
                {
                  "id": 6,
                  "name": "six",
                  "data": {
                    "5": "d"
                  }
                },
                {
                  "id": 7,
                  "name": null,
                  "data": null
                },
                {
                  "id": 8,
                  "name": "eight one",
                  "data": 555.555
                }
              ]
            ]
            """,
            actual
        )
    }

    @Test
    fun mapCollection() {
        val actual = dataStream.distinct().collect(
            Collectors.toMap(
                { it },
                { it.name ?: "" }
            )
        )

        jsonAssertEquals(
            """
            {
              "Rec(id=6, name=six, data=[a, 1, 3.3])": "six",
              "Rec(id=6, name=six, data={5=d})": "six",
              "Rec(id=2, name=second, data=null)": "second",
              "Rec(id=3, name=null, data=null)": "",
              "Rec(id=5, name=name five, data=[a, 21, 3.3])": "name five",
              "Rec(id=7, name=null, data=null)": "",
              "Rec(id=4, name=Four four, data=[a, 1, 3.3])": "Four four",
              "Rec(id=1, name=first, data=null)": "first",
              "Rec(id=3, name=same as 3333, data=three)": "same as 3333",
              "Rec(id=8, name=eight one, data=555.555)": "eight one",
              "Rec(id=4, name=four, data=444)": "four"
            }
            """,
            actual
        )
    }

    @Test
    fun mapCollectionById() {
        val actual = dataStream.distinct().collect(
            Collectors.toMap(
                { it.id },
                { it.name ?: "" },
                { n1, _ -> n1 }
            )
        )

        jsonAssertEquals(
            """
            {
              "1": "first",
              "2": "second",
              "3": "",
              "4": "Four four",
              "5": "name five",
              "6": "six",
              "7": "",
              "8": "eight one"
            }
            """,
            actual
        )
    }
}
