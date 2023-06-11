/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test

import java.util.Optional

interface Route {
    val baseUrlFromEnvironmentVariable: String
    val baseUrlDefault: String
    val path: String

    val route: String
        get() = "$baseUrl/$path"

    val baseUrl: String
        get() = baseUrl(baseUrlFromEnvironmentVariable, baseUrlDefault)

    private fun baseUrl(urlFromEnvVar: String, urlDefault: String) = Optional.ofNullable(
        System.getenv(urlFromEnvVar)
    ).orElse(urlDefault)
}
