/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web.controller

import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@Tag("functional")
@SpringBootTest(webEnvironment = RANDOM_PORT)
internal class WorkControllerFunctionalTest(
    @LocalServerPort val port: Int,
) : WorkControllerFunctionalTestAbstract(port) {
    companion object {

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            FunctionalTestAbstract.properties(registry)

            registry.add("keycloak.enabled") { "false" }
            registry.add("spring.autoconfigure.exclude") {
                "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration," +
                        "org.springframework.boot.actuate.autoconfigure.security.servlet" +
                        ".ManagementWebSecurityAutoConfiguration"
            }
        }
    }
}
