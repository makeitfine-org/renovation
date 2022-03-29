/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.api.test

import java.util.*

enum class ServerRoute(private val path: String) {
    API_WORK("api/work"),
    API_WORK_ID("api/work/{id}");

    companion object {
        @JvmStatic
        private val BASE_URL = Optional.ofNullable(System.getenv("SERVER_URL"))
                //todo: extract it from configs
            .orElse("http://localhost:8085")
    }

    fun getRoute() = "$BASE_URL/$path"
}
