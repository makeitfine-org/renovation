/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.api.test.frontend.info

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Tag
import renovation.backend.api.test.ApiTest
import renovation.backend.api.test.helper.FileHelper
import renovation.common.util.Json.given
import renovation.common.util.Json.rowJson

@Tag("e2e")
internal class FrontendInfoApiTest : ApiTest {

    @Test
    fun `worker`() {
        given()
            .When {
                get(FrontendInfoServerRoute.WORKER.route)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)

                val expected = FileHelper.WORKER_RESPONSE.fileContent
                body(equalTo(rowJson(expected)))
            }
    }

    @Test
    fun `graphql`() {
        given()
            .When {
                get(FrontendInfoServerRoute.GRAPHQL.route)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)

                val expected = FileHelper.GRAPHQL_RESPONSE.fileContent
                body(equalTo(rowJson(expected)))
            }
    }
}
