/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.CoreMatchers
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import renovation.backend.IntegrationTest
import kotlin.test.Test

@IntegrationTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class StartupControllerTest(
    @LocalServerPort val port: Int
) {
    val portHost = "http://localhost:$port"

    @Test
    fun `Assert startup controller`() {
        When {
            get(portHost + "/project")
        }.Then {
            statusCode(SC_OK)
            body(CoreMatchers.equalTo("Hi, it's \"Renovation\" project"))
        }
    }
}
