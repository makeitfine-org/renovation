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

    override val token: String = RestTemplate().let {
        val httpHeaders = HttpHeaders().also {
            it.contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val map = LinkedMultiValueMap<String, String>()
        map["grant_type"] = grantType
        map["client_id"] = clientId
        map["client_secret"] = clientSecret
        map["username"] = username
        map["password"] = password
        val token: AccessToken? =
            it.postForObject(
                tokenEndpoint,
                HttpEntity(
                    map,
                    httpHeaders
                ),
                AccessToken::class.java
            )

        token!!.accessToken
    }
}
