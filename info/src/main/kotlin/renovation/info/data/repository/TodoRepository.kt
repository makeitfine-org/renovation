/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import renovation.info.data.entity.TodoEntity

@Repository
interface TodoRepository : MongoRepository<TodoEntity, Int>
