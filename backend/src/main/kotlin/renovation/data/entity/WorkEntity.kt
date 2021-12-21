/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

package renovation.data.entity

import lombok.NoArgsConstructor
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "work")
@NoArgsConstructor
data class WorkEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var title: String,
    @Column(name = "\"desc\"")
    var desc: String? = null,
    var endDate: LocalDate? = null,
    var price: Double? = null,
    var payDate: LocalDate? = null,
)
