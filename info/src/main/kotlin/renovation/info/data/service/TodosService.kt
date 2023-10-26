/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.service

import renovation.info.data.model.TodosEntity
import renovation.info.generated.dgs.types.Details

interface TodosService {
    fun getAll(): List<TodosEntity>

    fun getById(id: Int): TodosEntity

    fun save(todosEntity: TodosEntity): TodosEntity
}
