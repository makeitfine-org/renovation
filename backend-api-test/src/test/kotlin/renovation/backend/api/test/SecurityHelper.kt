/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.api.test

import renovation.common.iam.impl.ClientCredentialsGrantAccessToken
import renovation.common.iam.impl.PasswordGrantAccessToken

object SecurityHelper {

    const val CLIENT_ID = "renovation-client"
    const val CLIENT_SECRET = "341b3ff9-7af0-4854-b024-63a82e7174cd"
    const val USERNAME = "all-test"
    const val PASSWORD = "test"
    const val TOKEN_ENDPOINT = "http://localhost:18080/realms/renovation-realm/protocol/openid-connect/token"


    @JvmStatic
    fun getPasswordGrantAccessToken() = PasswordGrantAccessToken(
        CLIENT_ID,
        CLIENT_SECRET,
        USERNAME,
        PASSWORD,
        TOKEN_ENDPOINT
    )

    @JvmStatic
    fun getClientCredentialsGrantAccessToken() = ClientCredentialsGrantAccessToken(
        CLIENT_ID,
        CLIENT_SECRET,
        TOKEN_ENDPOINT
    )
}
