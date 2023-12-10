/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2023
 */

package renovation.info.web.controller

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@Tag("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class StartupControllerTest(
    @LocalServerPort val port: Int
) {
    val portHost = "http://localhost:$port"

    @Test
    fun `Assert startup controller`() {
        When {
            get("$portHost/module")
        }.Then {
            statusCode(HttpStatus.SC_OK)
            body(
                CoreMatchers.equalTo("Hi, it's \"Renovation info\" module")
            )
        }
    }

    @Test
    fun `Assert about controller`() {
        When {
            get("$portHost/about")
        }.Then {
            statusCode(HttpStatus.SC_OK)
            body(
                CoreMatchers.equalTo(
                    """{"name":"renovation info module",""" +
                        """"description":"Module work as additional info directory"}"""
                )
            )
        }
    }
}
