/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.service

import renovation.info.data.entity.TodoEntity

interface TodoService {
    fun getAll(): List<TodoEntity>

    fun getById(id: Int): TodoEntity

    fun save(todoEntity: TodoEntity): TodoEntity
}
