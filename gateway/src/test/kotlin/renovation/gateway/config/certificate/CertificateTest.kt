/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.gateway.config.certificate

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
import renovation.common.util.Rest.given

@Tag("integrationTest")
@ActiveProfiles("certificate")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CertificateTest(
    @LocalServerPort private val port: Int,
) {

    @BeforeTest
    fun init() {
        RestAssured.baseURI = "https://localhost:$port"
        RestAssured.requestSpecification = given().relaxedHTTPSValidation("TLS")
    }

    @Test
    fun `Assert user page`() { // todo: e2e (ui test) with oauth2 login by github/google
        When {
            get("/certificate/user")
        }.Then {
            statusCode(HttpStatus.SC_OK)

            body("$.size()", Matchers.greaterThan(0))

            body(CoreMatchers.containsString("Login with OAuth 2.0"))

            body(CoreMatchers.containsString("GitHub"))
            body(CoreMatchers.containsString("/oauth2/authorization/github"))

            body(CoreMatchers.containsString("Google"))
            body(CoreMatchers.containsString("/oauth2/authorization/google"))
        }
    }

    @Test
    fun `Assert logout page`() {
        When {
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
