/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.web.controller.functional

import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import renovation.backend.KeycloakContainerConfig
import renovation.common.security.iam.GrantTypeAccessToken
import renovation.common.security.iam.impl.PasswordGrantTypeAccessToken

@Tag("integrationTest")
@ActiveProfiles("secured-test")
@ContextConfiguration(
    classes = [
        KeycloakContainerConfig::class,
        WorkControllerWithSecurityTest.ControllerTestConfig::class
    ]
)
@SpringBootTest(webEnvironment = RANDOM_PORT)
internal class WorkControllerWithSecurityTest(
    @LocalServerPort private val port: Int,
) : WorkControllerAppByContainersConfig(port) {

    @TestConfiguration
    class ControllerTestConfig {

        @Bean("workControllerToken")
        fun token(
            @Value("\${spring.security.oauth2.client.provider.oauth-client.token-uri}")
            tokenUri: String,
            @Value("\${spring.security.oauth2.client.registration.oauth-client.client-id}")
            clientId: String,
            @Value("\${spring.security.oauth2.client.registration.oauth-client.client-secret}")
            clientSecret: String,
            apiWork: ApiWork,
        ) = PasswordGrantTypeAccessToken(
            clientId,
            clientSecret,
            apiWork.username,
            apiWork.password,
            tokenUri
        )
    }

    @Autowired
    @Qualifier("workControllerToken")
    private lateinit var token: GrantTypeAccessToken

    override fun given() = super.given().let {
        val header = token.bearerAuthorizationHeader()

        println(header.headerValue)

        it.and()
            .header(header.headerName, header.headerValue)
    }
}

@Configuration
@ConfigurationProperties(prefix = "iam.api-work")
class ApiWork {
    lateinit var username: String
    lateinit var password: String
}
