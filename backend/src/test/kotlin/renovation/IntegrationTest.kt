/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation

import org.junit.jupiter.api.Tag
import org.springframework.test.context.ActiveProfiles

@Tag("integration")
@ActiveProfiles("itest")
annotation class IntegrationTest()
