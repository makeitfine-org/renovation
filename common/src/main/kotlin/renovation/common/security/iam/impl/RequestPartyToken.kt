/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.common.security.iam.impl

import renovation.common.security.iam.GrantTypeAccessToken
import renovation.common.security.iam.GrantTypeAccessToken.TokenFetcher

data class RequestPartyToken(
    // RPT
    override val tokenEndpoint: String,
    val jwtToken: String,
    val audience: String,
) : GrantTypeAccessToken {
    override val grantType = "urn:ietf:params:oauth:grant-type:uma-ticket"

    override val token: String = TokenFetcher.fetch(
        grantType = grantType,
        tokenEndpoint = tokenEndpoint,

        mapOf("Authorization" to "Bearer $jwtToken"),

        "audience" to audience,
    )
}
