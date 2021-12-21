/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.web

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Tag("integration")
class StartupControllerTest(
    @LocalServerPort val port: Int
) {
    val portHost = "http://localhost:${port}"

    @Test
    fun `Assert startup controller`() {
        When {
            get(portHost + "/")
        }.Then {
            statusCode(SC_OK)
            body(CoreMatchers.equalTo("Hi, it's \"Renovation\" project"))
        }
    }
}
