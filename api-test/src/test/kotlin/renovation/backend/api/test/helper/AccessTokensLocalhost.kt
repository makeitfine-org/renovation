/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.backend.api.test.helper

import renovation.common.security.iam.impl.ClientCredentialsGrantTypeAccessToken
import renovation.common.security.iam.impl.PasswordGrantTypeAccessToken

object AccessTokensLocalhost : AccessTokens {
    @JvmStatic
    val CLIENT_ID = System.getenv("KEYCLOAK_CLIENT_ID") ?: "renovation-client"

    @JvmStatic
    val CLIENT_SECRET = System.getenv("KEYCLOAK_CLIENT_SECRET") ?: "341b3ff9-7af0-4854-b024-63a82e7174cd"

    @JvmStatic
    val USERNAME = System.getenv("KEYCLOAK_USERNAME") ?: "all-test"

    @JvmStatic
    val PASSWORD = System.getenv("KEYCLOAK_PASSWORD") ?: "test"

    @JvmStatic
    val TOKEN_ENDPOINT = System.getenv("KEYCLOAK_TOKEN_ENDPOINT")
        ?: "http://localhost:18080/realms/renovation-realm/protocol/openid-connect/token"

    override fun getPasswordGrantAccessToken() = PasswordGrantTypeAccessToken(
        CLIENT_ID,
        CLIENT_SECRET,
        USERNAME,
        PASSWORD,
        TOKEN_ENDPOINT
    )

    override fun getClientCredentialsGrantAccessToken() = ClientCredentialsGrantTypeAccessToken(
        CLIENT_ID,
        CLIENT_SECRET,
        TOKEN_ENDPOINT
    )
}
