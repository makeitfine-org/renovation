/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.web.controller

import io.restassured.module.kotlin.extensions.Given
import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import renovation.common.security.iam.GrantTypeAccessToken
import renovation.common.security.iam.impl.ClientCredentialsGrantTypeAccessToken

@Tag("smoke")
@SpringBootTest(webEnvironment = RANDOM_PORT)
internal class DetailsControllerWithClientCredentialsGrantSecurityTest(
    @LocalServerPort private val port: Int
) : DetailsControllerTestAbstract(port) {

    @TestConfiguration
    class ControllerTestConfig {

        @Bean
        fun clientCredentialsGrantAccessToken(
            @Value("\${spring.security.oauth2.client.registration.client.client-id}")
            clientId: String,
            @Value("\${spring.security.oauth2.client.registration.client.client-secret}")
            clientSecret: String,
            @Value("\${spring.security.oauth2.client.provider.client.token-uri}")
            tokenUri: String,
        ) = ClientCredentialsGrantTypeAccessToken(
            clientId,
            clientSecret,
            tokenUri,
        )
    }

    @Autowired
    private lateinit var token: GrantTypeAccessToken

    override fun given() = Given {
        val header = token.bearerAuthorizationHeader()

        super.given()
            .and()
            .header(header.headerName, header.headerValue)
    }
}
