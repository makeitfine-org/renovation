/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.service

import renovation.backend.data.domain.Work

interface WorkService {
    fun findAll(): List<Work>

    fun findByTitleLike(titleLikePattern: String): List<Work>

    fun findById(id: Long): Work

    fun save(work: Work)

    fun update(id: Long, work: Work)

    fun delete(id: Long)
}
