/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.model

import org.springframework.data.mongodb.core.index.Indexed

data class AdditionInfoEntity(
    val nickName: String?,
    @Indexed(unique = true)
    val phoneNumber: String?,
)
