/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.api.test.frontend.info.serverapi

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Tag
import renovation.backend.api.test.frontend.info.FrontendInfoFileHelper
import renovation.backend.api.test.frontend.info.FrontendInfoServerRoute
import kotlin.test.Test

@Tag("smoke")
internal class FrontendInfoApiTest {
    companion object {
        @JvmStatic
        private val OBJECT_MAPPER = ObjectMapper()
    }

    @Test
    fun `worker`() {
        given()
            .When {
                get(FrontendInfoServerRoute.WORKER.route)
            }
            .Then {
                statusCode(HttpStatus.SC_OK)

                val expected = FrontendInfoFileHelper.WORKER_RESPONSE.fileContent
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

                val expected = FrontendInfoFileHelper.GRAPHQL_RESPONSE.fileContent
                body(equalTo(rowJson(expected)))
            }
    }

    // todo: move to util module (to think to create it previously)
    private fun rowJson(prettyJson: String) = OBJECT_MAPPER.readTree(prettyJson).toString()

    private fun given() = Given {
        header("Content-type", "application/json")
    }
}
