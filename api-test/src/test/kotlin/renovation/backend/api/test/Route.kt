/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test

import java.util.Optional

interface Route {

    fun baseUrl(envVarName: String, url: String) = Optional.ofNullable(
        System.getenv(envVarName)
    ).orElse(url)

    val BASE_URL: String

    val route: String
}
