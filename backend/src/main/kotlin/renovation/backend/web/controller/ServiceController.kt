/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.web.controller

import mu.KotlinLogging
import org.springframework.cache.annotation.CacheEvict
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val LOG = KotlinLogging.logger { }

@CrossOrigin(originPatterns = ["http://localhost:80*", "http://r"])
@RestController
@RequestMapping("/api/service")
@Validated
class ServiceController {

    @GetMapping("/redis/work/evict")
    @CacheEvict(value = ["works"], allEntries = true)
    fun evictWork() = LOG.info("evict/remove 'works' keys redis entities")
}
