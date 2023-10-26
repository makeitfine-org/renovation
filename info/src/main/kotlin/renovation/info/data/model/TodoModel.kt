/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.model

import java.time.LocalDateTime

data class TodoModel(
    val id: Int,
    val title: String,
    val completed: Boolean,
    val date: LocalDateTime,
)
