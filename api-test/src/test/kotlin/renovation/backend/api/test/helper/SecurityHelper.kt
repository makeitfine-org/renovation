/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2023
 */

package renovation.backend.api.test.helper

import renovation.common.security.iam.GrantTypeAccessToken

object SecurityHelper {

    @JvmStatic
    private val accessTokens: AccessTokens = System.getenv("ACCESS_TOKENS_LOCALHOST").let {
        if (it != null && it.lowercase() == "true") {
            AccessTokensLocalhost
        } else {
            AccessTokensDocker
        }
    }

    @JvmStatic
    fun obtainPasswordGrantAccessToken(): GrantTypeAccessToken = accessTokens.getPasswordGrantAccessToken()

    @JvmStatic
    fun obtainClientCredentialsGrantAccessToken(): GrantTypeAccessToken =
        accessTokens.getClientCredentialsGrantAccessToken()
}
