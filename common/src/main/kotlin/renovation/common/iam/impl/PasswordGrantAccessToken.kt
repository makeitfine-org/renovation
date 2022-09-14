/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.common.iam.impl

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import renovation.common.iam.GrantTypeAccessToken
import renovation.common.iam.GrantTypeAccessToken.AccessToken

data class PasswordGrantAccessToken(
    val clientId: String,
    val clientSecret: String,
    val username: String,
    val password: String,
    val tokenEndpoint: String,
) : GrantTypeAccessToken {
    override val grantType = "password"

    override val token: String = LinkedMultiValueMap<String, String>().let {
        it["grant_type"] = grantType
        it["client_id"] = clientId
        it["client_secret"] = clientSecret
        it["username"] = username
        it["password"] = password

        RestTemplate().postForObject(
            tokenEndpoint,
            HttpEntity(
                it,
                HttpHeaders().also {
                    it.contentType = MediaType.APPLICATION_FORM_URLENCODED
                }
            ),
            AccessToken::class.java
        )!!.accessToken
    }
}
