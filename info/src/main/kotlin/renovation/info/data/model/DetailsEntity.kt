/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import renovation.info.generated.dgs.types.Gender

@Document(collection = "details")
data class DetailsEntity(
    @JsonSerialize(using = ToStringSerializer::class)
    @Id
    var id: ObjectId? = ObjectId.get(),

    @field:NotEmpty
    @field:Size(min = 2, max = 25, message = "Name size should have not less 2 and not more 25 symbols")
    var name: String? = null,

    @field:NotEmpty
    @field:Size(min = 2, max = 25, message = "Surname size should have not less 2 and not more 25 symbols")
    var surname: String? = null,

    @field:NotNull
    @field:Min(18, message = "Age should be from 18")
    @field:Max(90, message = "Age should be up to 90")
    var age: Int? = null,

    @field:NotNull
    var gender: Gender? = null,

    val detailsEmails: List<DetailsEmailEntity>? = null,
    val additionInfos: List<AdditionInfoEntity>? = null
)
