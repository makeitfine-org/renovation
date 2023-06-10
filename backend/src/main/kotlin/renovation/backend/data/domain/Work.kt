/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.data.domain

import java.io.Serializable
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import renovation.backend.data.validation.OnCreate
import renovation.backend.data.validation.OnUpdate

data class Work(
    val id: String? = null,

    @field:NotBlank(groups = [OnCreate::class, OnUpdate::class])
    @field:Size(
        groups = [OnCreate::class, OnUpdate::class],
        message = "title symbol amount should be from 3 to 150", min = 3, max = 150
    )
    val title: String? = null,

    val description: String? = null,
    val endDate: LocalDate? = null,
    val price: Double? = null,
    val payDate: LocalDate? = null,
) : Serializable {
    companion object {
        const val serialVersionUID = 1L
    }
}
