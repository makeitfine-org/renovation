/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.kotlin

import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinConcepts {

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
}
