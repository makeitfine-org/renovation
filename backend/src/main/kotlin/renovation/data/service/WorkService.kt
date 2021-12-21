/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.data.service

import renovation.data.entity.WorkEntity

interface WorkService {
    fun findAll(): List<WorkEntity>

    fun findById(id: Long): WorkEntity?

    fun save(entity: WorkEntity): WorkEntity

    fun delete(id: Long)

    fun existsById(id: Long): Boolean
}
