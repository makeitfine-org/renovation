/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import renovation.backend.data.domain.Work
import renovation.backend.data.exception.WorkNotFoundException
import renovation.backend.data.service.WorkService
import java.util.*

@Service
class WorkServiceCacheableImpl(@Autowired @Qualifier("workServiceImpl") val workService: WorkService) :
    WorkService by workService {

    @Throws(WorkNotFoundException::class)
    @Cacheable(value = ["works"], key = "#id", unless = "#result.price > 10000")
    override fun findById(id: UUID) = workService.findById(id)

    @CachePut(value = ["works"], key = "#result.id", unless = "#result.price > 10000")
    override fun save(work: Work) = workService.save(work)

    @Throws(WorkNotFoundException::class)
    @CacheEvict(value = ["works"], key = "#id")
    override fun update(id: UUID, work: Work) = workService.update(id, work)

    @Throws(WorkNotFoundException::class)
    @CacheEvict(value = ["works"], key = "#id")
    override fun delete(id: UUID) = workService.delete(id)
}
