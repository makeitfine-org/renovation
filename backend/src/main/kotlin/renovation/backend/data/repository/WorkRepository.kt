/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import renovation.backend.data.entity.WorkEntity
import java.util.*

@Repository
interface WorkRepository : JpaRepository<WorkEntity, UUID> {
    fun findByTitleLike(titleLikePattern: String): List<WorkEntity>
}
