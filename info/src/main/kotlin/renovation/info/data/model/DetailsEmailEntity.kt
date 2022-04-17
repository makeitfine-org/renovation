/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.model

import org.springframework.data.mongodb.core.index.Indexed
import renovation.info.generated.dgs.types.EmailStatus

data class DetailsEmailEntity(
    @Indexed(unique = true)
    var email: String? = null,
    var emailStatus: EmailStatus? = null,
)
