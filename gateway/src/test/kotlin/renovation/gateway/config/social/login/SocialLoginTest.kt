/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.gateway.config.social.login

import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import renovation.common.util.Json
import renovation.common.util.Rest.given

@Tag("integrationTest")
@ActiveProfiles("social-login")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class SocialLoginTest(
    @LocalServerPort private val port: Int,
) {

    @BeforeTest
    fun init() {
        RestAssured.port = port
    }

    @Test
    fun `Assert about page`() {
        given(port)
            .When {
                get("/about")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(
                    CoreMatchers.equalTo(
                        Json.rowJson(
                            """
                            {
                                "name": "renovation gateway module",
                                "description": "Gateway for routing/gathering different request"
                            }
                            """
                        )
                    )
                )
            }
    }

    @Test
    fun `Assert user page`() {
        given(port)
            .When {
                get("/user")
            }.Then {
                statusCode(HttpStatus.SC_OK)

                body("$.size()", Matchers.greaterThan(0))
                body(CoreMatchers.containsString("Username"))
                body(CoreMatchers.containsString("Password"))
                body(CoreMatchers.containsString("Sign in"))

                body(CoreMatchers.containsString("Login with OAuth 2.0"))

                body(CoreMatchers.containsString("GitHub"))
                body(CoreMatchers.containsString("/oauth2/authorization/github"))

                body(CoreMatchers.containsString("Google"))
                body(CoreMatchers.containsString("/oauth2/authorization/google"))
            }
    }

    @Test
    fun `Assert logout page`() {
        given(port)
            .When {
                get("/logout")
            }.Then {
                statusCode(HttpStatus.SC_OK)

                body("$.size()", Matchers.greaterThan(0))

                body(CoreMatchers.containsString("Confirm Log Out?"))
                body(CoreMatchers.containsString("Are you sure you want to log out?"))

                body(CoreMatchers.containsString("/logout"))
            }
    }
}
