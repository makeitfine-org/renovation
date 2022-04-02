/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.entity

import java.time.LocalDate
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "work")
data class WorkEntity(
    @Id
    var id: UUID? = UUID.randomUUID(),
    var title: String,
    var description: String? = null,
    var endDate: LocalDate? = null,
    var price: Double? = null,
    var payDate: LocalDate? = null,
)
