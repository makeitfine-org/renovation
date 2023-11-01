/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.model

data class TodoModel(
    val id: Int,
    val title: String,
    val completed: Boolean,

    var description: String? = null,
    var category: String? = null,
    var image: String? = null,
    var price: Double? = null,
    var rating: Rating? = null,

    val date: String,
)
