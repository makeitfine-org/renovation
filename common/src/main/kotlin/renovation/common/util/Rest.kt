/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2023
 */

package renovation.common.util

import io.restassured.module.kotlin.extensions.Given
import renovation.common.security.iam.GrantTypeAccessToken

object Rest {
    @JvmStatic
    fun given() = Given {
        header("Content-type", "application/json")
    }

    @JvmStatic
    fun given(port: Int) = Given {
        port(port)
            .and().header("Content-type", "application/json")
    }

    @JvmStatic
    fun given(port: Int, token: GrantTypeAccessToken) = Given {
        token.bearerAuthorizationHeader().let {
            given(port)
                .and()
                .header(it.headerName, it.headerValue)
        }
    }
}
