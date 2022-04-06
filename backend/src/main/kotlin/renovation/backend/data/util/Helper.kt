/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.util

import renovation.backend.data.domain.Work
import renovation.backend.data.entity.WorkEntity
import java.util.*

class Helper {
    companion object {
        fun convert(workEntity: WorkEntity) =
            Work(
                id = workEntity.id?.toString(),
                title = workEntity.title,
                description = workEntity.description,
                endDate = workEntity.endDate,
                price = workEntity.price,
                payDate = workEntity.payDate
            )

        fun convert(work: Work) =
            WorkEntity(
                id = work.id?.let { UUID.fromString(it) } ?: UUID.randomUUID(),
                title = work.title?.let { it } ?: throw RuntimeException("title must be defined"),
                description = work.description,
                endDate = work.endDate,
                price = work.price,
                payDate = work.payDate
            )
    }
}