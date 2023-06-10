/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test.frontend.info

import renovation.backend.api.test.Route

enum class FrontendInfoServerRoute(path: String) : Route {
    WORKER("worker"),
    GRAPHQL("graphql");

    override val BASE_URL = baseUrl("FRONTEND_INFO_SERVER_URL", "http://localhost:8281")

    override val route = "$BASE_URL/$path"
}
