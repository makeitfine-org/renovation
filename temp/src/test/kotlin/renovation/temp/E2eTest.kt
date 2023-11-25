/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp

import org.junit.jupiter.api.Tag
import org.springframework.test.context.ActiveProfiles

@Tag("e2eTest")
@ActiveProfiles("e2e-test", "vault")
annotation class E2eTest
