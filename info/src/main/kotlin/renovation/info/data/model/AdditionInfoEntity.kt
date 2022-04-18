/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.model

import org.springframework.data.mongodb.core.index.Indexed

data class AdditionInfoEntity(
    var nickName: String? = null,
    @Indexed(unique = true)
    var phoneNumber: String? = null,
)
