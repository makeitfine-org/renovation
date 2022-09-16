/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.web.controller

import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
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
            registry.add("spring.autoconfigure.exclude") {
                "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration," +
                        "org.springframework.boot.actuate.autoconfigure.security.servlet" +
                        ".ManagementWebSecurityAutoConfiguration"
            }
        }
    }
}
