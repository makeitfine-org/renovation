/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

package renovation.backend.data.service.impl

import java.util.UUID
import mu.KotlinLogging
import org.springframework.ai.client.AiClient
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import renovation.backend.data.service.AIService

private val log = KotlinLogging.logger { }

@Service
class AIServiceImpl(private val aiClient: AiClient) : AIService {

    @Cacheable(value = ["ai"], key = "#id")
    override fun answer(id: UUID) =
        aiClient.generate("What year did Joseph Stalin died?").also {
            log.debug { "new request without caching" }
        }
}
