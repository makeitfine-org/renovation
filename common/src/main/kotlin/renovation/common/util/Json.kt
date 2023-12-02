/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2023
 */

package renovation.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.stream.Stream
import kotlin.test.assertEquals

object Json {

    @JvmStatic
    val OBJECT_MAPPER = ObjectMapper()

    /**
     * Pretty json presentation (with formatting) to simple one (no foramtting)
     */
    @JvmStatic
    fun rowJson(prettyJson: String, objectMapper: ObjectMapper = OBJECT_MAPPER): String =
        objectMapper.readTree(prettyJson).toString()

    /**
     * Object to json string simple representation (no formatting)
     */
    @Suppress("detekt:MemberNameEqualsClassName")
    fun json(obj: Any, objectMapper: ObjectMapper = OBJECT_MAPPER): String = objectMapper.writeValueAsString(obj)

    /**
     * assert equality of string json presentation object belong to the object
     *
     * @param obj - could be as obj, as collection, as stream,
     * if {@param obj} is stream, it's transformed to list
     */
    fun jsonAssertEquals(jsonPresentation: String, obj: Any): Unit = assertEquals(
        rowJson(jsonPresentation),
        json(
            if (obj is Stream<*>) {
                obj.toList()
            } else {
                obj
            }
        )
    )
}
