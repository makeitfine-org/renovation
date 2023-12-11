/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.common.security.iam

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import renovation.common.web.Client.RestClient

interface GrantTypeAccessToken : Token {
    val grantType: String
    val tokenEndpoint: String
    override val token: String

    fun bearerAuthorizationHeader() = Header("Authorization", "Bearer $token")

    data class Header(
        val headerName: String,
        val headerValue: String
    )

    /**
     * Take grant type access token from server
     */
    class TokenFetcher {
        companion object {

            @JvmStatic
            fun fetch(
                grantType: String,
                tokenEndpoint: String,
                vararg keyValues: Pair<String, String>
            ) = LinkedMultiValueMap<String, String>().let {
                keyValues.forEach { kv ->
                    it[kv.first] = kv.second
                }

                it["grant_type"] = grantType

                RestClient.postForObject(
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

        private class AccessToken {
            @Suppress("MemberNameEqualsClassName")
            val accessToken: String

            @JsonCreator
            constructor(@JsonProperty("access_token") accessToken: String) {
                this.accessToken = accessToken
            }
        }
    }
}
