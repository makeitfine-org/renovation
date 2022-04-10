/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import renovation.info.data.repository.DetailsRepository
import renovation.info.data.service.DetailsService

@Service
class DetailsServiceImpl(
    @Autowired val detailsRepository: DetailsRepository
) : DetailsService {

    override fun getAll() = detailsRepository.findAll()
}
