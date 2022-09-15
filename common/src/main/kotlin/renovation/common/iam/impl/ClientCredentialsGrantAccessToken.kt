/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.common.iam.impl

import renovation.common.iam.GrantTypeAccessToken

data class ClientCredentialsGrantAccessToken(
    val clientId: String,
    val clientSecret: String,
    override val tokenEndpoint: String,
) : GrantTypeAccessToken {
    override val grantType = "password"

    override val token: String = super.obtainToken(
        "client_id" to clientId,
        "client_secret" to clientSecret,
    )
}
