/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.domain

import java.time.LocalDate

data class Work(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val endDate: LocalDate? = null,
    val price: Double? = null,
    val payDate: LocalDate? = null,
)
