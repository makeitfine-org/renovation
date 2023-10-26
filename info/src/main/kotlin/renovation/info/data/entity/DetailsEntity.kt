/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.entity

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import renovation.info.generated.dgs.types.Gender
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

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