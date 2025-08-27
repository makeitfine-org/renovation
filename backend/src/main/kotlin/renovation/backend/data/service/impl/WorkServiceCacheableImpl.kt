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
import org.springframework.cache.annotation.Caching
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

        const val CACHE_WORK_BY_ID = "workById"
        const val CACHE_WORKS_ALL = "worksAll"
        const val UNLESS_CACHE_WORK = "#result == null || #result.price > $MAX_PRICE_TO_CACHE"
    }

    @Cacheable(value = [CACHE_WORKS_ALL])
    override fun findAll() = workService.findAll()

    @Throws(WorkNotFoundException::class)
    @Cacheable(
        value = [CACHE_WORK_BY_ID],
        key = "#id",
        unless = UNLESS_CACHE_WORK
    )
    override fun findById(id: UUID) = workService.findById(id)

    @PutWorkCache
    @EvictAllWorksCache
    override fun save(work: Work) = workService.save(work)

    @Throws(WorkNotFoundException::class)
    @PutWorkCache
    @CacheEvict(value = [CACHE_WORK_BY_ID], key = "#id", condition = "#result.price >= $MAX_PRICE_TO_CACHE")
    @EvictAllWorksCache
    override fun update(id: UUID, work: Work) = workService.update(id, work)

    @Throws(WorkNotFoundException::class)
    @CacheEvict(value = [CACHE_WORK_BY_ID], key = "#id")
    @EvictAllWorksCache
    override fun delete(id: UUID) = workService.delete(id)
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Caching(
    evict = [
        CacheEvict(
            value = [WorkServiceCacheableImpl.CACHE_WORKS_ALL],
            allEntries = true
        )
    ]
)
annotation class EvictAllWorksCache

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@CachePut(
    value = [WorkServiceCacheableImpl.CACHE_WORK_BY_ID],
    key = "#result.id",
    unless = WorkServiceCacheableImpl.UNLESS_CACHE_WORK
)
annotation class PutWorkCache
