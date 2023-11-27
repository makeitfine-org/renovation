/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */
package renovation.temp.githubplay

import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger { }

@Component
class AppRunner(private val gitHubLookupService: GitHubLookupService) : CommandLineRunner {
    @Suppress("detekt:MagicNumber")
    @Throws(Exception::class)
    override fun run(vararg args: String) {
        // Start the clock
        val start = System.currentTimeMillis()

        // Kick of multiple, asynchronous lookups
        val page1 = gitHubLookupService.findUser(2)
        val page2 = gitHubLookupService.findUser(1)
        val page3 = gitHubLookupService.findUser(5)

        // Wait until they are all done
        CompletableFuture.allOf(page1, page2, page3).join()

        // Print results, including elapsed time
        log.info("Elapsed time: " + (System.currentTimeMillis() - start))
        log.info("--> " + page1.get())
        log.info("--> " + page2.get())
        log.info("--> " + page3.get())
    }
}
