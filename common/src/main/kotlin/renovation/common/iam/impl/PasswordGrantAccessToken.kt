/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.common.iam.impl

import renovation.common.iam.GrantTypeAccessToken

data class PasswordGrantAccessToken(
    val clientId: String,
    val clientSecret: String,
    val username: String,
    val password: String,
    override val tokenEndpoint: String,
) : GrantTypeAccessToken {
    override val grantType = "password"

    //todo: it's write jwt token iss not null
    override val token: String = super.obtainToken(
        "client_id" to clientId,
        "client_secret" to clientSecret,
        "username" to username,
        "password" to password,
    )
}
