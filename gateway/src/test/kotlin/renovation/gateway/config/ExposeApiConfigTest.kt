/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2023
 */

package renovation.gateway.config

import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles

@Tag("e2eTest")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ExposeApiConfigTest(
    @LocalServerPort private val port: Int,
) : ExposeApiConfigTestAbstract(port)
