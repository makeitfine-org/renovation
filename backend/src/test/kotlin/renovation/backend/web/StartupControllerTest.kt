/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.web

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import renovation.backend.KeycloakContainerConfig
import renovation.backend.PostgresContainerConfig
import renovation.backend.RedisContainerConfig

@Tag("integrationTest")
@ContextConfiguration(
    classes = [
        KeycloakContainerConfig::class,
        PostgresContainerConfig::class,
        RedisContainerConfig::class
    ]
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class StartupControllerTest(
    @LocalServerPort val port: Int
) {
    val portHost = "http://localhost:$port"

    @Test
    fun `Assert startup controller`() {
        When {
            get("$portHost/project")
        }.Then {
            statusCode(SC_OK)
            body(CoreMatchers.equalTo("Hi, it's \"Renovation backend\" project"))
        }
    }

    @Test
    fun `Assert about controller`() {
        When {
            get("$portHost/about")
        }.Then {
            statusCode(SC_OK)
            body(
                CoreMatchers.equalTo(
                    """{"name":"renovation backend module",""" +
                        """"description":"Main backend part of renovation project"}"""
                )
            )
        }
    }
}
