/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.repository

import org.springframework.data.mongodb.repository.MongoRepository
import renovation.info.data.model.DetailsEntity

interface DetailsRepository : MongoRepository<DetailsEntity, String>