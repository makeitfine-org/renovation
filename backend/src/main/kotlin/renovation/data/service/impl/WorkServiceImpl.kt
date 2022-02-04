/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.data.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import renovation.data.domain.Work
import renovation.data.entity.WorkEntity
import renovation.data.exception.WorkNotFoundException
import renovation.data.repository.WorkRepository
import renovation.data.service.WorkService
import renovation.data.util.Helper

@Service
class WorkServiceImpl(@Autowired val workRepository: WorkRepository) : WorkService {
    override fun findAll() = workRepository.findAll().stream().map { e -> Helper.convert(e) }.toList()

    override fun findByTitleLike(titleLikePattern: String) = workRepository.findByTitleLike(titleLikePattern)
        .stream().map { e -> Helper.convert(e) }.toList()

    override fun findById(id: Long) = workRepository.findById(id).orElse(null)
        ?.let { Helper.convert(it) } ?: throw WorkNotFoundException(id)

    override fun save(work: Work) {
        workRepository.save(Helper.convert(work))
    }

    override fun update(id: Long, work: Work) {
        val workEntityForUpdate = workRepository.findById(id).orElse(null)
            ?.let { it } ?: throw WorkNotFoundException(id)
        updateEntity(work, workEntityForUpdate)
        workRepository.save(workEntityForUpdate)
    }

    override fun delete(id: Long) {
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
