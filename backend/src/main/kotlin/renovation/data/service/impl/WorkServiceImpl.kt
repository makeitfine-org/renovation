/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.data.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import renovation.data.entity.WorkEntity
import renovation.data.repository.WorkRepository
import renovation.data.service.WorkService

@Service
class WorkServiceImpl(@Autowired val workRepository: WorkRepository) : WorkService {
    override fun findAll() = workRepository.findAll()

    override fun findById(id: Long): WorkEntity? = workRepository.findById(id).orElse(null)

    override fun save(entity: WorkEntity) = workRepository.save(entity)

    override fun delete(id: Long) = workRepository.deleteById(id)

    override fun existsById(id: Long) = workRepository.existsById(id)
}
