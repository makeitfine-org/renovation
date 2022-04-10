/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.Test

@SpringBootTest(classes = [ConsoleApplication::class])
@ActiveProfiles("import")
internal class ConsoleApplicationTest {
    @Test
    fun contextLoads() {
    }
}
