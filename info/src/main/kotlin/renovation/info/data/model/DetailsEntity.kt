/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import renovation.info.generated.dgs.types.Gender

@Document(collection = "details")
data class DetailsEntity(
    @Id
    var id: String? = null,
    var name: String? = null,
    var surname: String? = null,
    var age: Int? = null,
    var gender: Gender? = null,
)
