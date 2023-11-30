/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.ktor.server.data.models

import kotlinx.serialization.Serializable

@Serializable
class Book(
    val id: Int,
    val title: String,
    val author: String,
    val publications: String
)
