/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

package renovation.backend.data.service.impl

import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import renovation.backend.data.domain.Work
import renovation.backend.data.exception.WorkNotFoundException
import renovation.backend.data.service.WorkService

// todo: impl. cacheable for other methods
@Service
class WorkServiceCacheableImpl(@Autowired @Qualifier("workServiceImpl") val workService: WorkService) :
    WorkService by workService {

    companion object {
        const val MAX_PRICE_TO_CACHE = 10_000
    }

    @Throws(WorkNotFoundException::class)
    @Cacheable(value = ["works"], key = "#id", unless = "#result.price > $MAX_PRICE_TO_CACHE")
    override fun findById(id: UUID) = workService.findById(id)

    @CachePut(value = ["works"], key = "#result.id", unless = "#result.price > $MAX_PRICE_TO_CACHE")
    override fun save(work: Work) = workService.save(work)

    @Throws(WorkNotFoundException::class)
    @CachePut(value = ["works"], key = "#id", unless = "#result.price > $MAX_PRICE_TO_CACHE")
    override fun update(id: UUID, work: Work) = workService.update(id, work)

    @Throws(WorkNotFoundException::class)
    @CacheEvict(value = ["works"], key = "#id")
    override fun delete(id: UUID) = workService.delete(id)
}
