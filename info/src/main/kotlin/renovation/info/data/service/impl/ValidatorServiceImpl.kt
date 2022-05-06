/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.service.impl

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import renovation.info.data.service.ValidatorService
import javax.validation.Valid

@Service
@Validated
class ValidatorServiceImpl : ValidatorService {
    override fun <T> validate(@Valid validatedEntity: T) = validatedEntity
}
