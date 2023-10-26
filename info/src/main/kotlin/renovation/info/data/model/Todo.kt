/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.model

import java.io.Serializable

data class Todo(
    val id: Int,
    val title: String,
    val surname: String? = null,
    val age: Int? = null,
)
