/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.data.entity

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "work")
@EntityListeners(AuditingEntityListener::class)
@SQLDelete(sql = "UPDATE work SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
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

    var deleted: Boolean = false
)
