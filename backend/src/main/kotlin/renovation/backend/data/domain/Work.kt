/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.domain

import java.io.Serializable
import renovation.backend.data.validation.OnCreate
import java.time.LocalDate
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class Work(
    val id: String? = null,

    @field:NotEmpty(groups = [OnCreate::class])
    @field:Size(groups = [OnCreate::class], min = 3, max = 150, message = "title symbol amount should be from 3 to 150")
    val title: String? = null,

    val description: String? = null,
    val endDate: LocalDate? = null,
    val price: Double? = null,
    val payDate: LocalDate? = null,
): Serializable
