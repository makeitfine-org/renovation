/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.service.impl

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import renovation.info.data.model.DetailsEntity
import renovation.info.data.repository.DetailsRepository
import renovation.info.data.service.DetailsService
import renovation.info.generated.dgs.types.Details
import renovation.info.generated.dgs.types.DetailsInput

@Service
class DetailsServiceImpl(
    @Autowired val detailsRepository: DetailsRepository
) : DetailsService {

    override fun getAll() = detailsRepository.findAll()

    override fun save(detailsInput: DetailsInput) =
        detailsRepository.save(
            DetailsEntity(
                id = ObjectId.get().toHexString(),
                name = detailsInput.name,
                surname = detailsInput.surname,
                age = detailsInput.age,
                gender = detailsInput.gender
            )
        ).let {
            Details(
                id = it.id,
                name = it.name,
                surname = it.surname,
                age = it.age,
                gender = it.gender
            )
        }
}
