/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.temp.githubplay

import java.util.concurrent.CompletableFuture
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

const val DELAY = 1000L

@Service
class GitHubLookupService(restTemplateBuilder: RestTemplateBuilder) {
    private val restTemplate = restTemplateBuilder.build()

    @Async
    @Throws(InterruptedException::class)
    fun findUser(id: Int): CompletableFuture<User?> {
        logger.info("Looking up $id")
        val url = "https://jsonplaceholder.typicode.com/users/$id"
        val results = restTemplate.getForObject(url, User::class.java)
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(DELAY)
        return CompletableFuture.completedFuture(results)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GitHubLookupService::class.java)
    }
}
