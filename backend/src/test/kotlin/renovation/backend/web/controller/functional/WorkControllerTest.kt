/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.web.controller.functional

import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import renovation.backend.AppByContainersConfig

@Tag("integrationTest")
@SpringBootTest(webEnvironment = RANDOM_PORT)
internal class WorkControllerTest(
    @LocalServerPort val port: Int,
) : WorkControllerAppByContainersConfig(port) {
    companion object {

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            AppByContainersConfig.properties(registry)

            registry.add("keycloak.enabled") { "false" }
            registry.add("spring.autoconfigure.exclude") {
                "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration," +
                        "org.springframework.boot.actuate.autoconfigure.security.servlet" +
                        ".ManagementWebSecurityAutoConfiguration"
            }
        }
    }
}
