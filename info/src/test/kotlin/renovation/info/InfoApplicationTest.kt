/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

package renovation.info

import kotlin.test.Test
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest

@Tag("integrationTest")
@SpringBootTest(classes = [InfoApplication::class])
internal class InfoApplicationTest {
    @Test
    fun contextLoads() {
    }
}
