/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.data.entity

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "work")
@EntityListeners(AuditingEntityListener::class)
@SQLDelete(sql = "UPDATE work SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
data class WorkEntity(
    @Id
    var id: UUID? = UUID.randomUUID(),
    var title: String,
    var description: String? = null,
    var endDate: LocalDate? = null,
    var price: Double? = null,
    var payDate: LocalDate? = null,

    @CreatedDate
    var createdDate: LocalDateTime? = null,
    @LastModifiedDate
    var lastModifiedDate: LocalDateTime? = null,

    var deleted:Boolean = false
)
