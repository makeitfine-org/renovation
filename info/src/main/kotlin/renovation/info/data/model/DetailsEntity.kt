/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "details")
data class DetailsEntity(
    @Id
    val id: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val age: Int? = null,
    val gender: String? = null,
)
