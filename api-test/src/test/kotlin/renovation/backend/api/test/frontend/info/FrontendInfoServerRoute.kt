/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.api.test.frontend.info

import java.util.*

enum class FrontendInfoServerRoute(path: String) {
    WORKER("worker"),
    GRAPHQL("graphql");

    private val FRONTEND_INFO_BASE_URL = Optional.ofNullable(System.getenv("FRONTEND_INFO_SERVER_URL"))
        // todo: extract it from configs
        .orElse("http://localhost:8281")

    val route = "$FRONTEND_INFO_BASE_URL/$path"
}
