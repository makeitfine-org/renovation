/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.webflux.server.web

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import reactor.core.publisher.Mono

class ReactorPlay {

    @Test
    fun mono() {
        val nullable: () -> LocalDateTime? = {
            Thread.sleep(500)
            null
        }

        assertFailsWith<NullPointerException> {
            Mono.just(nullable()!!)
        }

        assertEquals(Mono.empty(), Mono.justOrEmpty(nullable()))
        assertEquals(LocalDateTime.MAX, Mono.justOrEmpty(nullable()).defaultIfEmpty(LocalDateTime.MAX).block())

        Mono.defer<LocalDateTime?> { null } // just example
        assertFailsWith<NullPointerException> {
            Mono.defer<LocalDateTime?> { null }.block()
        }

        assertEquals(5, Mono.defer { Mono.justOrEmpty(5) }.block())
        Mono.fromSupplier { 5 } // just example
        assertEquals(5, Mono.fromSupplier { 5 }.block())

        Mono.create { monoSink -> monoSink.success(nullable()) } // just example
        assertNull(Mono.create { monoSink -> monoSink.success(nullable()) }.block())
    }

    @Test
    @Suppress("detekt:TooGenericExceptionThrown")
    fun monoException() {
        val exceptional: () -> LocalDateTime? = {
            Thread.sleep(500)
            throw RuntimeException("ERRORS HAPPEN")
        }

        val e = assertFailsWith<RuntimeException> {
            Mono.just(exceptional()!!)
        }
        assertEquals("ERRORS HAPPEN", e.message)

        assertFailsWith<RuntimeException> {
            Mono.fromSupplier { exceptional() }.block()
        }

        assertFailsWith<RuntimeException> {
            Mono.create { monoSink -> monoSink.success(exceptional()) }.block()
        }
    }
}
