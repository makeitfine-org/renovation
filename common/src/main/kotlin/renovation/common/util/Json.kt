/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2023
 */

package renovation.common.util

import com.fasterxml.jackson.databind.ObjectMapper

object Json {

    @JvmStatic
    val OBJECT_MAPPER = ObjectMapper()

    @JvmStatic
    fun rowJson(prettyJson: String, objectMapper: ObjectMapper = OBJECT_MAPPER) =
        objectMapper.readTree(prettyJson).toString()
}
