/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.data.service

import org.springframework.stereotype.Service

data class Rec(
    val id: Long,
    val name: String? = null,
    val any: Any? = null,
)

@Service
class StreamService {
    companion object {
        @JvmStatic
        private val DATA = listOf(
            Rec(1, "first"),
            Rec(2, "second"),
            Rec(3),
            Rec(4, "Four four", listOf("a", 1, 3.3)),
            Rec(5, "name five", listOf("a", 21, 3.3)),
            Rec(6, "six", listOf("a", 1, 3.3)),
            Rec(7),
            Rec(8, "eight one", 555.555),
            Rec(3, "same as 3333", "three"),
            Rec(4, "four", 444),
        )
    }

    fun data() = DATA
}