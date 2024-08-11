/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

package renovation.common.security.iam.impl

import renovation.common.security.iam.GrantTypeAccessToken
import renovation.common.security.iam.GrantTypeAccessToken.TokenFetcher

data class ClientCredentialsGrantTypeAccessToken(
    val clientId: String,
    val clientSecret: String,
    override val tokenEndpoint: String,
) : GrantTypeAccessToken {
    override val grantType = "client_credentials"

    override val token: String = TokenFetcher.fetch(
        grantType = grantType,
        tokenEndpoint = tokenEndpoint,

        null,

        "client_id" to clientId,
        "client_secret" to clientSecret,
    )
}
