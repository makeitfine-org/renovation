/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.common.iam

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

interface GrantTypeAccessToken : Token {
    val grantType: String
    val tokenEndpoint: String
    override val token: String

    fun bearerAuthorizationHeader() =
        Header("Authorization", "Bearer $token")

    fun obtainToken(vararg keyValues: Pair<String, String>) = LinkedMultiValueMap<String, String>().let {
        keyValues.forEach { kv ->
            it[kv.first] = kv.second
        }

        it["grant_type"] = grantType

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
