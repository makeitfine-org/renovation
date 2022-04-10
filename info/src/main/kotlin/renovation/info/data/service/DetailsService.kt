/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.service

import renovation.info.data.model.DetailsEntity

interface DetailsService {
    fun getAll(): List<DetailsEntity>
}
