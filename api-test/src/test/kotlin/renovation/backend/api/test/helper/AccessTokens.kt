/*
 *  Created under not commercial project "Renovation"
 *
 *  Copyright 2021-2023
 */

package renovation.backend.api.test.helper

import renovation.common.security.iam.GrantTypeAccessToken

interface AccessTokens {
    fun getPasswordGrantAccessToken(): GrantTypeAccessToken

    fun getClientCredentialsGrantAccessToken(): GrantTypeAccessToken
}
