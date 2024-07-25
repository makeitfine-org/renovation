/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.web.controller

import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import renovation.backend.data.service.AIService

@RestController
@RequestMapping("ai")
class AIController(private val aiService: AIService) {

    companion object {
        @JvmStatic
        private val LOG = LoggerFactory.getLogger(AIController::class.java)
    }

    @GetMapping
    fun list(): String {
        LOG.info("ai answer")

        return aiService.answer(UUID.fromString("bf4088ab-754f-40cf-a904-3b69e2f6e614"))
    }
}
