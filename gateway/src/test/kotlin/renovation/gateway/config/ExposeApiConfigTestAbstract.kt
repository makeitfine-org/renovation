/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2023
 */

package renovation.gateway.config

import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlin.test.Test
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ContextConfiguration
import renovation.common.security.iam.GrantTypeAccessToken
import renovation.common.security.iam.impl.ClientCredentialsGrantTypeAccessToken
import renovation.common.util.Rest.given

@Suppress("UnnecessaryAbstractClass")
@ContextConfiguration(classes = [ExposeApiConfigTestAbstract.ControllerTestConfig::class])
internal abstract class ExposeApiConfigTestAbstract(
    private val port: Int,
) {
    @TestConfiguration
    class ControllerTestConfig {

        @Bean
        fun clientCredentialsGrantTypeAccessToken(
            @Value("\${spring.security.oauth2.client.registration.oauth-client.client-id}")
            clientId: String,
            @Value("\${spring.security.oauth2.client.registration.oauth-client.client-secret}")
            clientSecret: String,
            @Value("\${spring.security.oauth2.client.provider.oauth-client.token-uri}")
            tokenUri: String,
        ) = ClientCredentialsGrantTypeAccessToken(
            clientId,
            clientSecret,
            tokenUri
        )
    }

    @Autowired
    private lateinit var token: GrantTypeAccessToken

    @Test
    fun `Assert about controller`() {
        given(port)
            .When {
                get("/about")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(
                    CoreMatchers.equalTo(
                        """{"name":"renovation gateway module",""" +
                            """"description":"Gateway for routing/gathering different request"}"""
                    )
                )
            }
    }

    @Test
    fun `Assert user controller`() {
        given(port, token)
            .When {
                get("/user")
            }.Then {
                statusCode(HttpStatus.SC_OK)
                body(
                    CoreMatchers.equalTo("user")
                )
            }
    }

    @Test
    fun `Assert admin controller`() {
        given(port, token)
            .When {
                get("/admin")
            }.Then {
                statusCode(HttpStatus.SC_FORBIDDEN)
            }
    }
}
