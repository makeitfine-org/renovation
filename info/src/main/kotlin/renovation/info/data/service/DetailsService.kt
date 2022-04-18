/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.service

import renovation.info.data.model.DetailsEntity
import renovation.info.generated.dgs.types.Details
import renovation.info.generated.dgs.types.DetailsInput

interface DetailsService {
    fun getAll(): List<DetailsEntity>

    fun getById(id: String): DetailsEntity

    fun save(detailsInput: DetailsInput): Details
}
