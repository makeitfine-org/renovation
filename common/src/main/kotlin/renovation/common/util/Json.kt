/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2023
 */

package renovation.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.module.kotlin.extensions.Given

object Json {

    @JvmStatic
    val OBJECT_MAPPER = ObjectMapper()

    @JvmStatic
    fun rowJson(prettyJson: String, objectMapper: ObjectMapper = OBJECT_MAPPER) =
        objectMapper.readTree(prettyJson).toString()

    @JvmStatic
    fun given() = Given {
        header("Content-type", "application/json")
    }
}
