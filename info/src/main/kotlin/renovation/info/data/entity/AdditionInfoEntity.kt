/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.entity

import org.springframework.data.mongodb.core.index.Indexed

data class AdditionInfoEntity(
    val nickName: String?,
    @Indexed(unique = true)
    val phoneNumber: String?,
)
