/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.service

import javax.validation.Valid

interface ValidatorService {
    fun <T> validate(@Valid validatedEntity: T): T
}
