/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.api.test

import java.util.*

enum class ServerRoute(path: String) {
    ACTUATOR_HEALTH("actuator/health"),
    ACTUATOR_INFO("actuator/info"),

    API_WORK("api/work"),
    API_WORKER("api/worker"),
    API_WORK_ID("api/work/{id}");

    private val BASE_URL = Optional.ofNullable(System.getenv("SERVER_URL"))
        // todo: extract it from configs
        .orElse("http://localhost:8280")

    val route = "$BASE_URL/$path"
}
