/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.service.impl

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import renovation.info.data.model.AdditionInfoEntity
import renovation.info.data.model.DetailsEmailEntity
import renovation.info.data.model.DetailsEntity
import renovation.info.data.repository.DetailsRepository
import renovation.info.data.service.DetailsService
import renovation.info.data.service.ValidatorService
import renovation.info.generated.dgs.types.Details
import renovation.info.generated.dgs.types.DetailsEmail
import renovation.info.generated.dgs.types.DetailsInput

@Service
class DetailsServiceImpl(
    @Autowired val detailsRepository: DetailsRepository,
    @Autowired val validatorService: ValidatorService,
) : DetailsService {

    override fun getAll() = detailsRepository.findAll()

    override fun getById(id: String) = detailsRepository.findById(ObjectId(id)).orElseThrow()

    override fun save(detailsInput: DetailsInput) =
        detailsRepository.save(
            validatorService.validate(
                DetailsEntity(
                    id = ObjectId.get(),
                    name = detailsInput.name,
                    surname = detailsInput.surname,
                    age = detailsInput.age,
                    gender = detailsInput.gender,
                    detailsEmails = detailsInput.detailsEmails
                        ?.map {
                            DetailsEmailEntity(it?.email, it?.emailStatus)
                        }
                        ?.toList(),
                    additionInfos = detailsInput.additionInfos
                        ?.map {
                            AdditionInfoEntity(it?.nickName, it?.phoneNumber)
                        }
                        ?.toList()
                )
            )
        ).let {
            Details(
                id = it.id?.toHexString(),
                name = it.name,
                surname = it.surname,
                age = it.age,
                gender = it.gender,
                detailsEmails = it.detailsEmails
                    ?.map {
                        DetailsEmail(it.email, it.emailStatus)
                    }
                    ?.toList()
            )
        }
}
