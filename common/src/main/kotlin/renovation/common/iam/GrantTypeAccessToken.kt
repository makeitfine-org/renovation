/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.common.iam

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

interface GrantTypeAccessToken : Token {
    val grantType: String
    override val token: String

    fun bearerAuthorizationHeader() =
        Header("Authorization", "Bearer $token")

    data class Header(
        val headerName: String,
        val headerValue: String
    )

    class AccessToken {
        val accessToken: String

        @JsonCreator
        constructor(@JsonProperty("access_token") accessToken: String) {
            this.accessToken = accessToken
        }
    }
}
