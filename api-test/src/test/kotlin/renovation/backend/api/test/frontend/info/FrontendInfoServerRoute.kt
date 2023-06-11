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

    override val baseUrlFromEnvironmentVariable = "FRONTEND_INFO_SERVER_URL"
    override val baseUrlDefault = "http://localhost:8281"
    override val path = path
}
