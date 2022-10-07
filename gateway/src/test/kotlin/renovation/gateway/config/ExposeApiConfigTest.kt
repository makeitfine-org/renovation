/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.gateway.config

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import renovation.common.iam.GrantTypeAccessToken
import renovation.common.iam.impl.PasswordGrantAccessToken

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ExposeApiConfigTest(
    @LocalServerPort private val port: Int,
    @Autowired private val creds: AllTestUserCredentials,
) {
    val portHost = "http://localhost:$port"

    @TestConfiguration
    class ControllerTestConfig {

        @Bean
        fun passwordGrantAccessToken(
            @Value("\${spring.security.oauth2.client.registration.oauth-client.client-id}")
            clientId: String,
            @Value("\${spring.security.oauth2.client.registration.oauth-client.client-secret}")
            clientSecret: String,
            @Value("\${spring.security.oauth2.client.provider.oauth-client.token-uri}")
            tokenUri: String,
            creds: AllTestUserCredentials
        ) = PasswordGrantAccessToken(
            clientId,
            clientSecret,
            creds.username,
            creds.password,
            tokenUri
        )
    }

    @Autowired
    private lateinit var token: GrantTypeAccessToken

    fun given() = Given {
        token.bearerAuthorizationHeader().let {
            port(port)
                .and()
                .header(it.headerName, it.headerValue)
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
                    """{"name":"renovation gateway module","""
                            + """"description":"Gateway for routing/gathering different request"}"""
                )
            )
        }
    }

    @Test
    fun `Assert user controller`() {
        given()
            .When {
                get("$portHost/user")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(
                    CoreMatchers.equalTo("user")
                )
            }
    }
}

@Configuration
@ConfigurationProperties(prefix = "spring.security.credentials")
class AllTestUserCredentials {
    lateinit var username: String
    lateinit var password: String
}
