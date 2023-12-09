/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.model

import jakarta.validation.constraints.Min

data class Rating(
    @field:Min(MIN_PRIORITY, message = "min priory should be $MIN_PRIORITY")
    @field:Min(MAX_PRIORITY, message = "max priory should be $MAX_PRIORITY")
    val priority: Int = 0
) {
    companion object {
        const val MIN_PRIORITY = 0L
        const val MAX_PRIORITY = 5L
    }
}
