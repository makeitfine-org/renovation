/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test

import java.util.Optional

interface Route {
    val baseUrlEnvironmentVariableName: String
    val baseUrlEnvironmentVariableValue: String
    val path: String

    val route: String
        get() = "$baseUrl/$path"

    private val baseUrl: String
        get() = baseUrl(baseUrlEnvironmentVariableName, baseUrlEnvironmentVariableValue)

    private fun baseUrl(envVarName: String, url: String) = Optional.ofNullable(
        System.getenv(envVarName)
    ).orElse(url)
}
