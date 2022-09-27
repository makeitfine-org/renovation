/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.gateway.config

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ExposeApiConfigTest(
    @LocalServerPort val port: Int
) {
    val portHost = "http://localhost:$port"

    @Test
    fun `Assert about controller`() {
        When {
            get("$portHost/about")
        }.Then {
            statusCode(HttpStatus.SC_OK)
            body(
                CoreMatchers.equalTo(
                    """{"name":"renovation gateway module","""
                            + """"description":"Gateway for routing/gathering different request"}"""
                )
            )
        }
    }
}
