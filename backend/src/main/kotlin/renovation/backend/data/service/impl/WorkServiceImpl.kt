/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import renovation.backend.data.domain.Work
import renovation.backend.data.entity.WorkEntity
import renovation.backend.data.exception.WorkNotFoundException
import renovation.backend.data.repository.WorkRepository
import renovation.backend.data.service.WorkService
import renovation.backend.data.util.Helper
import java.util.*

@Service
class WorkServiceImpl(@Autowired val workRepository: WorkRepository) : WorkService {
    companion object {
        @JvmStatic
        private val SORT = Sort.by(Sort.Direction.ASC, "id")
    }

    override fun findAll() = workRepository
        .findAll(SORT).stream()
        .map(Helper::convert).toList()

    override fun findByTitleLike(titleLikePattern: String) = workRepository
        .findByTitleLike(titleLikePattern).stream()
        .map(Helper::convert).toList()

    @Throws(WorkNotFoundException::class)
    @Cacheable(value = ["works"], key = "#id", unless = "#result.price > 10000")
    override fun findById(id: UUID) = workRepository
        .findById(id).orElse(null)
        ?.let { Helper.convert(it) } ?: throw WorkNotFoundException(id)

    override fun save(work: Work) {
        workRepository.save(Helper.convert(work))
    }

    @Throws(WorkNotFoundException::class)
//    @CachePut(value = ["works"], key = "#id", unless = "#result.price > 10000")
//    @CachePut(value = ["works"], key = "#id")
    override fun update(id: UUID, work: Work) {
        val workEntityForUpdate = workRepository.findById(id).orElse(null)
            ?.let { it } ?: throw WorkNotFoundException(id)
        updateEntity(work, workEntityForUpdate)
        workRepository.save(workEntityForUpdate)
    }

    @Throws(WorkNotFoundException::class)
    @CacheEvict(value = ["works"], key = "#id")
    override fun delete(id: UUID) {
        if (!workRepository.existsById(id)) {
            throw WorkNotFoundException(id)
        }
        workRepository.deleteById(id)
    }

    private fun updateEntity(work: Work, workEntityForUpdate: WorkEntity) {
        work.title?.let { workEntityForUpdate.title = it }
        work.description?.let { workEntityForUpdate.description = it }
        work.endDate?.let { workEntityForUpdate.endDate = it }
        work.price?.let { workEntityForUpdate.price = it }
        work.payDate?.let { workEntityForUpdate.payDate = it }
    }
}
