/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.backend.api.test.helper

import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import renovation.common.security.iam.GrantTypeAccessToken

object AccessTokensDocker : AccessTokens {

    @JvmStatic
    private val URL = System.getenv("ACCESS_TOKENS_URL") ?: "http://localhost:8281/insecure/token/grant"

    @JvmStatic
    private fun getAccessToken(httRequest: HttpRequestBase): String {
        var token: String

        HttpClients.createDefault().use {
            it.execute(httRequest).use {
                token = EntityUtils.toString(it.entity);
            }
        }

        return token
    }

    @JvmStatic
    private fun grantTypeAccessToken(grantType: String) = object : GrantTypeAccessToken {
        override val grantType = grantType
        override val tokenEndpoint: String
            get() = TODO("Not yet implemented")
        override val token = getAccessToken(
            HttpGet(
                "$URL/${
                    if (grantType == "password") {
                        "password"
                    } else if (grantType == "client_credentials") {
                        "client"
                    } else {
                        NotImplementedError()
                    }
                }"
            )
        )
    }

    override fun getPasswordGrantAccessToken() = grantTypeAccessToken("password")

    override fun getClientCredentialsGrantAccessToken() = grantTypeAccessToken("client_credentials")
}
