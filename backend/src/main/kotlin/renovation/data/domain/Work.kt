/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.data.domain

import lombok.Builder
import lombok.NoArgsConstructor
import java.time.LocalDate


@NoArgsConstructor
@Builder
data class Work(
    val title: String? = null,
    val description: String? = null,
    val endDate: LocalDate? = null,
    val price: Double? = null,
    val payDate: LocalDate? = null,
)
