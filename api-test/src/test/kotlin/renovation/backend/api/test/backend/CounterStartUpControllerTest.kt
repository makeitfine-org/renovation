/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test.backend

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Tag
import renovation.backend.api.test.helper.SecurityHelper
import renovation.common.util.Json.rowJson
import renovation.common.util.Rest

@Tag("e2eTest")
internal class CounterStartUpControllerTest {
    @Test
    fun `counter`() {
        Given {
            val header = SecurityHelper.obtainPasswordGrantAccessToken().bearerAuthorizationHeader()

            Rest.given()
                .and()
                .header(header.headerName, header.headerValue)
        }.When {
            get(BackendServerRoute.COUNTER.route)
        }.Then {
            statusCode(HttpStatus.SC_OK)
            body(
                equalTo(
                    rowJson(
                        """
                        {
                            "counter": 1
                        }
                        """
                    )
                )
            )
        }
    }
}
