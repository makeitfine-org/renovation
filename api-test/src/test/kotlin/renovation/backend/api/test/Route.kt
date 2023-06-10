package renovation.backend.api.test

import java.util.Optional

interface Route {

    fun baseUrl(envVarName: String, url: String) = Optional.ofNullable(
        System.getenv(envVarName)
    ).orElse(url)

    val BASE_URL: String

    val route: String
}
