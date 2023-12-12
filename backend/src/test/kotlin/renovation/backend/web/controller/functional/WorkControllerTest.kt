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
import org.springframework.test.context.ActiveProfiles

@Tag("integrationTest")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("no-security")
internal class WorkControllerTest(
    @LocalServerPort val port: Int,
) : WorkControllerAppByContainersConfig(port)
