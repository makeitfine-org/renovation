/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.util

import renovation.info.data.entity.TodoEntity
import renovation.info.data.model.TodoModel

object Helper {

    @JvmStatic
    fun convert(todoEntity: TodoEntity) =
        TodoModel(
            id = todoEntity.id,
            title = todoEntity.title,
            completed = todoEntity.completed,
            date = todoEntity.date
        )

    @JvmStatic
    fun convert(todoModel: TodoModel) =
        TodoEntity(
            id = todoModel.id,
            title = todoModel.title,
            completed = todoModel.completed,
            date = todoModel.date
        )
}
