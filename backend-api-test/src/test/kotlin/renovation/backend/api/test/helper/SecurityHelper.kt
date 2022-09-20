/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.backend.api.test.helper

import renovation.common.iam.GrantTypeAccessToken

object SecurityHelper {

    @JvmStatic
    fun getPasswordGrantAccessToken(): GrantTypeAccessToken = AccessTokensLocalhost.getPasswordGrantAccessToken()

    @JvmStatic
    fun getClientCredentialsGrantAccessToken(): GrantTypeAccessToken =
        AccessTokensLocalhost.getClientCredentialsGrantAccessToken()
}
