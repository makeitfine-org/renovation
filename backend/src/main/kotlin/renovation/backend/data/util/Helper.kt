/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.data.util

import java.util.UUID
import renovation.backend.data.domain.Work
import renovation.backend.data.entity.WorkEntity
import renovation.backend.data.exception.UndefineTitleException

object Helper {

    @JvmStatic
    fun convert(workEntity: WorkEntity) =
        Work(
            id = workEntity.id?.toString(),
            title = workEntity.title,
            description = workEntity.description,
            endDate = workEntity.endDate,
            price = workEntity.price,
            payDate = workEntity.payDate
        )

    @JvmStatic
    fun convert(work: Work) =
        WorkEntity(
            id = work.id?.let { UUID.fromString(it) } ?: UUID.randomUUID(),
            title = work.title?.let { it } ?: failTitle(),
            description = work.description,
            endDate = work.endDate,
            price = work.price,
            payDate = work.payDate
        )

    private fun failTitle(): Nothing = throw UndefineTitleException()
}
