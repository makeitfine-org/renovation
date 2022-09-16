/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.web.controller

import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@Tag("smoke")
@SpringBootTest(webEnvironment = RANDOM_PORT)
internal class DetailsControllerTest(
    @LocalServerPort private val port: Int
) : DetailsControllerTestAbstract(port) {

    companion object {

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.security.enabled") { "false" }
        }
    }

    @TestConfiguration
    class ApplicationSecurity : WebSecurityConfigurerAdapter() {

        @Throws(Exception::class)
        override fun configure(http: HttpSecurity) {
            http.authorizeRequests { authorizeRequests ->
                authorizeRequests.antMatchers("/**").permitAll()
            }
        }
    }
}
