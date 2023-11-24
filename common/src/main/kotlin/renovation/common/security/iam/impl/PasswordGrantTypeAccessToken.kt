/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.common.security.iam.impl

import renovation.common.security.iam.GrantTypeAccessToken

data class PasswordGrantTypeAccessToken(
    val clientId: String,
    val clientSecret: String,
    val username: String,
    val password: String,
    override val tokenEndpoint: String,
) : GrantTypeAccessToken {
    override val grantType = "password"

    // todo: it's write jwt token iss not null
    override val token: String = super.obtainToken(
        "client_id" to clientId,
        "client_secret" to clientSecret,
        "username" to username,
        "password" to password,
    )
}
