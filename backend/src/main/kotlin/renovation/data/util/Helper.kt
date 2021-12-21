/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.data.util

import renovation.data.domain.Work
import renovation.data.entity.WorkEntity

class Helper {
    companion object {
        fun convert(workEntity: WorkEntity) =
            Work(
                title = workEntity.title,
                desc = workEntity.desc,
                endDate = workEntity.endDate,
                price = workEntity.price,
                payDate = workEntity.payDate
            )

        fun convert(work: Work) =
            WorkEntity(
                title = work.title?.let { it } ?: throw RuntimeException("title must be defined"),
                desc = work.desc,
                endDate = work.endDate,
                price = work.price,
                payDate = work.payDate
            )
    }
}
