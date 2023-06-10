/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.api.test.backend

import renovation.backend.api.test.Route

enum class BackendServerRoute(path: String) : Route {
    ACTUATOR_HEALTH("actuator/health"),
    ACTUATOR_INFO("actuator/info"),

    API_WORK("api/work"),
    API_WORKER("api/worker"),
    API_WORK_ID("api/work/{id}");

    override val BASE_URL = baseUrl("BACKEND_SERVER_URL", "http://localhost:8280")

    override val route = "$BASE_URL/$path"
}
