/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation

import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@Tag("integration")
@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
annotation class IntegrationTest()
