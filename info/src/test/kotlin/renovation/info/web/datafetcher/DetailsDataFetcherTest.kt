/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.web.datafetcher

import com.netflix.graphql.dgs.DgsQueryExecutor
import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@Tag("smoke")
@SpringBootTest
internal class DetailsDataFetcherTest(
    @Autowired
    private val dgsQueryExecutor: DgsQueryExecutor,
) : DetailsDataFetcherTestAbstract(dgsQueryExecutor) {

    companion object {

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.security.enabled") { "false" }
        }
    }
}
