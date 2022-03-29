/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import renovation.backend.data.entity.WorkEntity

@Repository
interface WorkRepository : CrudRepository<WorkEntity, Long> {
    override fun findAll(): List<WorkEntity>

    fun findByTitleLike(titleLikePattern: String): List<WorkEntity>
}
