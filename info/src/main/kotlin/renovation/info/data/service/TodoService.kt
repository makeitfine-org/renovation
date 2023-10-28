/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.service

import renovation.info.data.model.TodoModel

interface TodoService {
    fun getAll(): List<TodoModel>

    fun getById(id: Int): TodoModel

    fun save(todoModel: TodoModel): Unit

    fun update(todoModel: TodoModel): Unit

    fun delete(id: Int): Unit
}
