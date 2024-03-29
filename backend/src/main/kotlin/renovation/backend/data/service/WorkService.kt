/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.data.service

import java.util.UUID
import renovation.backend.data.domain.Work

interface WorkService {
    fun findAll(): List<Work>

    fun findByTitleLike(titleLikePattern: String): List<Work>

    fun findById(id: UUID): Work

    fun save(work: Work): Work

    fun update(id: UUID, work: Work): Work

    fun delete(id: UUID)
}
