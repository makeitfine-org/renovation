/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import renovation.info.generated.dgs.types.Gender

@Document(collection = "details")
data class DetailsEntity(
    @JsonSerialize(using = ToStringSerializer::class)
    @Id
    var id: ObjectId? = ObjectId.get(),
    var name: String? = null,
    var surname: String? = null,
    var age: Int? = null,
    var gender: Gender? = null,
    val detailsEmails: List<DetailsEmailEntity>? = null,
    val additionInfos: List<AdditionInfoEntity>? = null
)
