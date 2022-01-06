/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.data.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import renovation.data.entity.WorkEntity

@Repository
interface WorkRepository : CrudRepository<WorkEntity, Long> {
    override fun findAll(): List<WorkEntity>

    fun findByTitleLike(titleLikePattern: String): List<WorkEntity>
}
