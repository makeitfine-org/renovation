/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.entity

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import renovation.info.data.model.Rating

@Document(collection = "todos")
data class TodoEntity(
    @Id
    var id: Int,

    @field:NotBlank
    @field:Size(
        min = MIN_TITLE_SIZE,
        max = MAX_TITLE_SIZE,
        message = "Name size should have not less $MIN_TITLE_SIZE and not more $MAX_TITLE_SIZE symbols"
    )
    var title: String,

    @field:NotNull
    var completed: Boolean,

    @field:Size(
        min = MIN_DESCRIPTION_SIZE,
        message = "description size should be not less then ${MIN_DESCRIPTION_SIZE} chapters"
    )
    var description: String? = null,

    var category: String? = null,

    var image: String? = null,

    @field:DecimalMin("0.0", message = "price should be zero or positive")
    var price: BigDecimal? = null,

    var rating: Rating? = null,

    @field:NotNull
    var date: LocalDateTime
) {
    companion object {
        const val MIN_TITLE_SIZE = 2
        const val MAX_TITLE_SIZE = 50
        const val MIN_DESCRIPTION_SIZE = 3
    }
}
