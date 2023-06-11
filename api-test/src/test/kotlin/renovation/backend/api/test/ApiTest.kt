/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test

import com.fasterxml.jackson.databind.ObjectMapper

interface ApiTest {

    val OBJECT_MAPPER: ObjectMapper
        get() = ObjectMapper()
}
