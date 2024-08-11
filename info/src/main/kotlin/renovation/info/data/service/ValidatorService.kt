/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

package renovation.info.data.service

import jakarta.validation.Valid

interface ValidatorService {
    fun <T> validate(@Valid validatedEntity: T): T
}
