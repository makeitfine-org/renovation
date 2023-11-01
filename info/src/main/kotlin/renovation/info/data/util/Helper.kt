/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import renovation.info.data.entity.TodoEntity
import renovation.info.data.model.TodoModel

object Helper {

    @JvmStatic
    val TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    @JvmStatic
    fun convert(todoEntity: TodoEntity) =
        TodoModel(
            id = todoEntity.id,
            title = todoEntity.title,
            completed = todoEntity.completed,

            description = todoEntity.description,
            category = todoEntity.category,
            image = todoEntity.image,
            price = todoEntity.price?.toDouble() ?: null,

            rating = todoEntity.rating,

            date = todoEntity.date.toString(),
        )

    @JvmStatic
    fun convert(todoModel: TodoModel) =
        TodoEntity(
            id = todoModel.id,
            title = todoModel.title,
            completed = todoModel.completed,

            description = todoModel.description,
            category = todoModel.category,
            image = todoModel.image,
            price = todoModel.price?.toBigDecimal() ?: null,

            rating = todoModel.rating,

            date = LocalDateTime.parse(todoModel.date, TIME_FORMATTER)
        )
}
