/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.model

import org.springframework.data.mongodb.core.index.Indexed
import renovation.info.generated.dgs.types.EmailStatus

data class DetailsEmailEntity(
    @Indexed(unique = true)
    val email: String?,
    val emailStatus: EmailStatus?,
)
