/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.util

import renovation.backend.data.domain.Work
import renovation.backend.data.entity.WorkEntity
import renovation.backend.data.exception.UndefineTitleException
import java.util.*

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
            title = work.title?.let { it } ?: throw UndefineTitleException(),
            description = work.description,
            endDate = work.endDate,
            price = work.price,
            payDate = work.payDate
        )
}
