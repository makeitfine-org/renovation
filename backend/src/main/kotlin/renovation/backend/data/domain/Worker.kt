/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.backend.data.domain

import java.io.Serializable

data class Worker(
    val id: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val age: Int? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
