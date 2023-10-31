/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.entity

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import jakarta.validation.constraints.NotBlank
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import renovation.info.generated.dgs.types.Gender
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Document(collection = "details")
data class DetailsEntity(
    @JsonSerialize(using = ToStringSerializer::class)
    @Id
    var id: ObjectId? = ObjectId.get(),

    @field:NotBlank
    @field:Size(min = 2, max = 25, message = "Name size should have not less 2 and not more 25 symbols")
    var name: String? = null,

    @field:NotBlank
    @field:Size(min = 2, max = 25, message = "Surname size should have not less 2 and not more 25 symbols")
    var surname: String? = null,

    @field:NotNull
    @field:Min(value = MIN_AGE, message = "Age should be from 18")
    @field:Max(value = MAX_AGE, message = "Age should be up to 90")
    var age: Int? = null,

    @field:NotNull
    var gender: Gender? = null,

    val detailsEmails: List<DetailsEmailEntity>? = null,
    val additionInfos: List<AdditionInfoEntity>? = null,
) {
    companion object {
        const val MIN_AGE = 18L
        const val MAX_AGE = 90L
    }
}
