/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import renovation.info.data.entity.DetailsEntity

@Repository
interface DetailsRepository : MongoRepository<DetailsEntity, ObjectId>
