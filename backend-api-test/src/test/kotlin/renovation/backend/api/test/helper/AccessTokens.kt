/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.backend.api.test.helper

import renovation.common.iam.GrantTypeAccessToken

interface AccessTokens {
    fun getPasswordGrantAccessToken(): GrantTypeAccessToken

    fun getClientCredentialsGrantAccessToken(): GrantTypeAccessToken
}
