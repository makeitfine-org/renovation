/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.kotlin

import java.util.Arrays
import kotlin.random.Random
import kotlin.reflect.KProperty
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.assertDoesNotThrow
import renovation.common.util.Json
import kotlin.text.StringBuilder as sbu

class KotlinConcepts {

    @Test
    fun numbers() {
        assertTrue(2.7183 is Double)
        assertTrue(2.7183f is Float)
        assertTrue(22 is Int)
        assertTrue(22L is Long)
        assertTrue(22.toShort() is Short)
        assertTrue(22.toByte() is Byte)

        val un = 100u
        assertTrue(un is UInt)
        assertTrue(3.toULong() is ULong)

        val n: Int? = 100
        val n2: Int? = 1000
        val n2NonNull: Int = n2!!
        assertTrue(n === 100)
        assertTrue(n2NonNull === 1000)
        assertTrue(1000 === 1000)
    }

    @Test
    fun multipleStringStrict() {
        val actualText =
            """for (c in "foo")
    print(c)"""
        assertEquals("for (c in \"foo\")\n    print(c)", actualText)
    }

    @Test
    fun multipleStringNewLine() {
        val actualText =
            """
            for (c in "foo")
                print(c)
            """
        assertEquals("\n            for (c in \"foo\")\n                print(c)\n            ", actualText)
    }

    @Test
    fun multipleStringWhitespaces() {
        val actualText =
            """
            | for (c in "foo")
            |    print(c)
            """.trimMargin()
        assertEquals(" for (c in \"foo\")\n    print(c)", actualText)
    }

    @Test
    fun multipleStringOtherMergeWhitespaces() {
        val actualText =
            """
            >> for (c in "foo")
            >>    print(c)
            """.trimMargin(">>")
        assertEquals(" for (c in \"foo\")\n    print(c)", actualText)
    }

    @Test
    fun templateStringScreen() {
        val actualText =
            """
            ${5 + 5} ${'$'}_10.99
            """.trimIndent()
        assertEquals("10 ${'$'}_10.99", actualText)
    }

    @Test
    @Suppress("detekt:ImplicitDefaultLocale")
    fun stringFormatting() {
        assertEquals(String.format("%08d", 315722283), "315722283")
        assertEquals(String.format("%08d", 22283), "00022283")

        assertEquals(String.format("%+.3f", 22.33283), "+22.333")
        assertEquals(String.format("%+.3f", 22.33253), "+22.333")
        assertEquals(String.format("%+.3f", 22.33203), "+22.332")
        assertEquals(String.format("%+.3f", 22.33243), "+22.332")

        assertEquals(String.format("%.2f", 22.33283), "22.33")
        assertEquals(String.format("%.2f", 22.33253), "22.33")
        assertEquals(String.format("%.2f", 22.33503), "22.34")
        assertEquals(String.format("%.2f", 22.33843), "22.34")

        assertEquals(String.format("%s %s   %s", "abc", "ok", "2"), "abc ok   2")
    }

    @Test
    fun array() {
        assertEquals("1, 2, 3", arrayOf(1, 2, 3).joinToString())
        assertEquals("1 ! 2 ! 3", arrayOf(1, 2, 3).joinToString(separator = " ! "))

        assertTrue(Array(3) { 0 } contentEquals arrayOf(0, 0, 0))
        assertTrue(Array(5) { i -> (i * i).toString() } contentEquals arrayOf("0", "1", "4", "9", "16"))

        Json.jsonAssertEquals(
            """
            [
              [
                [
                  0,
                  0,
                  0
                ],
                [
                  0,
                  0,
                  0
                ],
                [
                  0,
                  0,
                  0
                ]
              ],
              [
                [
                  0,
                  0,
                  0
                ],
                [
                  0,
                  0,
                  0
                ],
                [
                  0,
                  0,
                  0
                ]
              ],
              [
                [
                  0,
                  0,
                  0
                ],
                [
                  0,
                  0,
                  0
                ],
                [
                  0,
                  0,
                  0
                ]
              ]
            ]
            """,
            Array(3) { Array(3) { Array(3) { 0 } } }
        )

        assertEquals(6, (Array(3) { i -> (i * 2) }).sum())
    }

    @Test
    fun typeCheck() {
        assertTrue(5 is Int)
        assertFalse(Any() is Long)
        assertTrue(Any() !is Long)
    }

    @Test
    fun smartTypeCheck() {
        val a: Any = listOf(5, 5, -5)

        if (a is Collection<*>) {
            Assertions.assertThat(a.size).isEqualTo(3)
        }

        val aOther: Any = listOf(5, 5, -5)
        if (aOther !is Collection<*>) {
            return
        }

        Assertions.assertThat(aOther.size).isEqualTo(3)
    }

    @Test
    fun typeCast() {
        val x = "abc"
        val y: String = x as String
        assertEquals(y, "abc")

        assertFailsWith<ClassCastException> {
            val y: Int = x as Int
        }
        assertNull(x as? Int)
        assertNull(x as? Int?)

        val a: Any = "sbc"
        assertEquals((a as? String)?.length ?: -3, 3)
        assertEquals((a as? Int)?.toByte() ?: -3, -3)
    }

    @Test
    fun equality() {
        val a = 100
        val b = 100

        // same
        assertTrue(a === b)
        assertSame(a, b)

        // same
        assertTrue(a.equals(b))
        assertTrue(a == b)

        val a10001 = 10001
        val b10001 = 10001

        // same
        assertTrue(a10001 === b10001)
        assertNotSame(a10001, b10001)

        // same
        assertTrue(a10001.equals(b10001))
        assertTrue(a10001 == b10001)

        val aNull = null
        val bNull = null

        // same
        assertTrue(aNull === bNull)
        assertSame(aNull, bNull)

        // same
        assertTrue(aNull.equals(bNull))
        assertTrue(aNull == bNull)
    }

    @Test
    fun boolNot() {
        val a = true
        assertEquals(!a, a.not())
    }

    @Test
    fun inOperation() {
        val a = 10
        assertTrue(a in -1..10)

        assertFalse(a in -1..9)
        assertTrue(a !in -1..9)
    }

    @Test
    fun ifElseVariants() {
        var actual: Int? = null

        // same
        Assertions.assertThat(
            if (actual != null) {
                actual
            } else {
                5
            }
        ).isEqualTo(5)
        Assertions.assertThat(actual ?: 5).isEqualTo(5)
        Assertions.assertThat(actual.takeIf { it != null } ?: 5).isEqualTo(5)
        Assertions.assertThat(actual.takeIf { actual != null } ?: 5).isEqualTo(5)

        actual = 7

        // same
        Assertions.assertThat(
            if (actual != null) {
                actual
            } else {
                5
            }
        ).isEqualTo(7)
        Assertions.assertThat(actual ?: 5).isEqualTo(7)
        Assertions.assertThat(actual.takeIf { it != null } ?: 5).isEqualTo(7)
        Assertions.assertThat(actual.takeIf { actual != null } ?: 5).isEqualTo(7)
    }

    @Test
    fun whenTest() {
        var a = 5
        var actual = when (a) {
            3 -> 0
            5 -> 1
            else -> throw IllegalArgumentException()
        }

        assertEquals(1, actual)

        a = 7
        assertFailsWith<IllegalArgumentException> {
            actual = when (a) {
                3 -> 0
                5 -> 1
                else -> throw IllegalArgumentException()
            }
        }
    }

    @Test
    fun cycles() {
        var sum = 0
        for (i in -1..3) {
            sum += i
        }
        assertEquals(5, sum)

        sum = 0
        for (i in -1..5 step 2) {
            sum += i
        }
        assertEquals(8, sum)

        sum = 0
        for (i in 5 downTo -2 step 3) {
            sum += i
        }
        assertEquals(6, sum)

        sum = 0
        while (sum < 5) {
            sum += 1
        }
        assertEquals(5, sum)

        sum = 0
        do {
            sum += 1
        } while (sum < 5)
        assertEquals(5, sum)
    }

    @Test
    fun breakContinue() {
        var sum = 0
        for (i in -1..3) {
            sum += i
            break
        }
        assertEquals(-1, sum)

        sum = 0
        for (i in -1..5 step 2) {
            continue
            sum += i
        }
        assertEquals(0, sum)
    }

    @Test
    fun ranges() {
        val r1 = 1..5
        assertEquals(r1.toList(), listOf(1, 2, 3, 4, 5))

        val r2 = -1..6 step 2
        assertEquals(r2.toList(), listOf(-1, 1, 3, 5))

        val r3 = 10 downTo 1 step 3
        assertEquals(r3.toList(), listOf(10, 7, 4, 1))
    }

    @Test
    fun arrays() {
        val arr = arrayOf("a", "11", 1)
        assertTrue(5 !in arr)
        assertTrue("11" in arr)

        assertTrue(arrayOf("a", "11", 1) contentEquals arr)
        assertFalse(arrayOf("a", "11", 1) contentEquals null)

        assertTrue(Array(3) { -1 } contentEquals arrayOf(-1, -1, -1))
        assertTrue(Array(3) { i -> (i + 1) * 2 } contentEquals arrayOf(2, 4, 6))
    }

    private fun example(first: String, second: Int) = "$first $second"
    private fun exampleOther(first: String = "!", second: Int) = "$first $second"
    private fun exampleOtherOther(second: Int, first: String = "!") = "$first $second"
    private fun vaArg(vararg str: String = arrayOf("!")) =
        Arrays.stream(str).reduce { s1, s2 -> "$s1 | $s2" }.get()

    @Test
    fun functions() {
        assertEquals("abc 11", example("abc", 11))
        assertEquals("abc 11", example(first = "abc", second = 11))
        assertEquals("abc 11", example(second = 11, first = "abc"))

        assertEquals("abc 111", exampleOther(second = 111, first = "abc"))
        assertEquals("! 111", exampleOther(second = 111))
        assertEquals("! 111", exampleOtherOther(111))

        assertEquals("a | b | cd", vaArg("a", "b", "cd"))
        assertEquals("!", vaArg("!"))
        val arr = arrayOf("1", "2", "75")
        assertEquals("1 | 2 | 75", vaArg(*arr))

        fun inside(i: Int) = "$i++"
        assertEquals("5++", inside(5))

        val f: (i: Int, s: String) -> Double = { i, s -> "$i$s".length / 2.0 }
        assertEquals(2.5, f(33, "asc"))
        val fVariant = { i: Int, s: String -> "$i$s".length / 2.0 }
        assertEquals(2.5, fVariant(33, "asc"))

        val oper = ::inside
        assertEquals("7++", oper(7))

        val fOther: (s: String) -> Double = { "$it".length / 2.0 }
        assertEquals(2.5, fOther("ssasc"))
        val fOtherVariant: (s: String) -> Double = { "$it".length / 2.0 }
        assertEquals(2.5, fOtherVariant("22asc"))

        fun insideOther(s: String, f: (i: Int) -> String) = "${s.uppercase()} ${f(5)}"

        assertEquals("ABC 5++", insideOther("aBc", ::inside))
        assertEquals("ABC 5++", insideOther("aBc", { inside(it) }))
        assertEquals("ABC 5++", insideOther("aBc") { inside(it) })

        val anon = fun(s: String) = s.length
        assertEquals(5, anon("abc12"))
        val anonVar2 = { s: String -> s.length }
        assertEquals(5, anonVar2("abc12"))
        val anonVar3: (s: String) -> Int = { it.length }
        assertEquals(5, anonVar3("abc12"))

        val l = { "abc" }
        assertEquals("abc", l())
        val lVar2 = { -> "abc" }
        assertEquals("abc", lVar2())

        val un = { _: Int, _: String -> "sss" }
        assertEquals("sss", un(3, "s"))
        val unVar2: (Int, String) -> String = { _, _ -> "sss" }
        assertEquals("sss", unVar2(3, "s2"))
    }

    class One(val m: Int) {
        private val s: String
        private val i = 10
        private var d: Double = 5.5

        init {
            s = "abc"
        }

        constructor() : this(52) {
            d = 2.2
        }

        constructor(d: Double) : this() {
            this.d = d
        }

        @Suppress("detekt:UnusedPrivateMember")
        constructor(m: Long) : this(5.5) {
            d = 1.5
        }
    }

    @Suppress("detekt:MemberNameEqualsClassName")
    open class A {
        open fun a() {}
    }

    open class B : A() {
        final override fun a() {
        }
    }

    class C : B()

    class M {
        val num = 55

        class N
        inner class I {
            val k = this@M.num
        }
    }

    @Test
    fun classes() {
        assertDoesNotThrow {
            One()
            One(5.5)
            One(5L)
            One(5L)

            A().a()
            B().a()
            C().a()

            M()
            M.N()
            M().I()
        }

        assertEquals("abca", sbu("abc").append("a").toString())
    }

    data class Data(val i: Int, val s: String)

    @Test
    fun dataClass() {
        assertEquals("Data(i=5, s=!)", Data(5, "!").toString())
        assertEquals(Data(1, "!"), Data(5, "!").copy(i = 1))

        val (i, s) = Data(100, "abc")
        assertEquals(100, i)
        assertEquals("abc", s)
    }

    enum class E {
        E1,
        E2
    }

    @Test
    fun enums() {
        assertEquals(1, E.E2.ordinal)
        assertEquals("E1", E.E1.name)
        assertEquals(E.E2, E.valueOf("E2"))
        assertTrue { arrayOf(E.E1, E.E2) contentEquals E.values() }
    }

    interface D {
        fun l(a: String): Int
    }

    class D1 : D {
        override fun l(a: String) = a.length
    }

    open class D2(d1: D1, s: String) : D by d1, Comparable<String> by s

    @Test
    fun delegation() {
        assertEquals(3, D2(D1(), "").l("abc"))
    }

    @Test
    fun objectTest() {
        val o = object : D2(D1(), "") {
            override fun l(a: String) = 0
        }

        assertEquals(0, o.l(Random.nextInt(5).toString()))
    }

    @Suppress("detekt:UnusedPrivateMember")
    class Cl<T : Comparable<T>, M>(t: T, m: M) {
        fun some(t1: T, t2: T) = t1 > t2
    }

    @Suppress("detekt:UnusedPrivateMember")
    fun <T : Number, S> fu(t1: T, t2: T, s: S? = null): Byte where T : Comparable<T>, S : String {
        return t1.toByte().plus(t2.toByte()).toByte()
    }

    @Test
    fun generics() {
        assertFalse(Cl(5, "").some(7, 8))
        assertEquals(15, fu(7.5, 8.75, ""))
    }

    open class Message(val text: String)
    interface Messenger<out T : Message> {
        fun writeMessage(text: String): T
    }

    class EmailMessage(text: String) : Message(text)

    @Test
    @Suppress("detekt:UnusedPrivateMember")
    fun covariance() {
        assertDoesNotThrow {
            val message: Messenger<Message>? = null
            val messageOther: Messenger<Message> = object : Messenger<EmailMessage> {
                override fun writeMessage(text: String) = EmailMessage("ddd")
            }
        }
    }

    open class MessageCounter(val text: String)
    interface MessengerCounter<in T : Message> {
        fun writeMessage(t: T)
    }

    class EmailMessageCounter(text: String) : MessageCounter(text)

    @Test
    @Suppress("detekt:UnusedPrivateMember")
    fun countervariance() {
        assertDoesNotThrow {
            val message: MessengerCounter<EmailMessage>? = null
            val messageOther: MessengerCounter<EmailMessage> = object : MessengerCounter<Message> {
                override fun writeMessage(t: Message) = TODO("Not yet implemented")
            }
        }
    }

    @Test
    fun extensions() {
        fun String.uc() = this.uppercase()
        fun String.reversed() = this.lowercase()

        assertEquals("abc", "aBc".reversed())
        assertEquals("ABC", "abc".uc())
    }

    @Test
    fun operators() {
        class A(var a: Int) {
            operator fun unaryMinus() = a--.let { this }
            operator fun unaryPlus() = a++.let { this }
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as A

                return a == other.a
            }

            override fun hashCode(): Int {
                return a
            }
        }

        val a = A(5)
        assertEquals(A(4), a.unaryMinus())
        assertEquals(A(5), a.unaryPlus())
    }

    class LoggerDelegate(private var personAge: Int) {
        operator fun getValue(thisRef: Person, property: KProperty<*>): Int {
            println("get Person.age")
            return personAge
        }

        operator fun setValue(thisRef: Person, property: KProperty<*>, value: Int) {
            println("set Person.age: $value")
            personAge = value
        }
    }

    class Person(val name: String, _age: Int) {
        var age: Int by LoggerDelegate(_age)
    }

    @Test
    fun delegationProp() {
        assertDoesNotThrow {
            val p = Person("abc", 5)
            p.age
            p.age = 5
        }
    }

    @Test
    fun scopeFunctions() {
        assertEquals("cba", "abc".let { it.reversed() })

        val p = Person("name", 75)
        val va = with(p) {
            age = 5
            this.age = 5
            1
        }
        assertEquals(p.age, 5)
        assertEquals(va, 1)

        val pRun = Person("name", 35)
        val vaRun = pRun.run {
            age = 5
            this.age = 5
            15
        }
        assertEquals(pRun.age, 5)
        assertEquals(vaRun, 15)

        assertSame(
            pRun,
            pRun.apply {
                age = 1
                this.age = 5
            }
        )
        assertEquals(5, pRun.age)

        assertSame(pRun, pRun.also { it.age = 1 })
        assertSame(1, pRun.age)
    }

    inline fun <T, R> T.letUC(block: (T) -> R): R {
        println(this.toString().uppercase())
        return block(this)
    }

    inline fun <T> T.letMy(block: (T) -> String): String = "${block(this)} !!!"

    @Test
    fun myExtensions() {
        assertEquals("abc", "abc".letUC { it })
        assertEquals(51, 51.letUC { it })

        assertEquals("51 !!!", 51.letMy { it.toString() })
        assertEquals("51.5 !!!", 51.5.letMy { it.toString() })
    }

    class FoInf {
        infix fun inf(i: Int): String = Array(i) { "0" }.joinToString(separator = "")
        infix fun infSame(i: Int): String = "0".repeat(i)
    }

    @Test
    fun infixTest() {
        assertEquals("00000", FoInf() inf 5)
        assertEquals("000", FoInf() infSame 3)
    }

    @Test
    fun collections() {
        val l = mutableListOf("")
        l.add("s")
        l.add("sd")
        assertEquals(l, listOf("", "s", "sd"))

        val l1 = listOf(1, 5, 8)
        val l2 = listOf(1, 8, 19)
        assertEquals(l1.union(l2), setOf(1, 5, 8, 19))
        assertEquals(l1.intersect(l2), setOf(1, 8))
        assertEquals(l1.subtract(l2), setOf(5))

        assertEquals(mapOf(5 to "a", 1 to "c"), mapOf(5 to "a", 1 to "b", 1 to "c"))
        assertEquals(listOf("a", "b", "c", "d"), listOf("a", "b", "c") + "d")
        assertEquals(listOf("a", "b", "c"), listOf("a", "b", "c") - "m")
        assertEquals(listOf("a", "c", "b"), listOf("a", "b", "c", "b") - "b")
        assertEquals(listOf("a", "c"), listOf("a", "b", "c", "b") - listOf("b", "b"))

        assertDoesNotThrow {
            val dictionary = listOf("red", "blue", "green")
                .zip(listOf("красный", "синий", "зеленый"))
            val words = dictionary.unzip()
            println(words.first) // [red, blue, green]
            println(words.second)
            dictionary[0]
        }

        val people = listOf("Tom", "Sam", "Kate", "Bob", "Alice")
        assertEquals("Kate", people.first { it.length == 4 })
        assertEquals("Bob", people.last { it.length == 3 })
    }
}
