/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.entity

import java.time.LocalDateTime
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Document(collection = "todos")
data class TodoEntity(
    @Id
    var id: Int,

    @field:NotBlank
    @field:Size(min = 2, max = 50, message = "Name size should have not less 2 and not more 50 symbols")
    var title: String,

    @field:NotNull
    var completed: Boolean,

    @field:NotNull
    var date: LocalDateTime
)
