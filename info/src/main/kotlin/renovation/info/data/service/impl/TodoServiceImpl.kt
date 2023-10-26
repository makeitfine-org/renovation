/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.service.impl

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import renovation.info.data.entity.TodoEntity
import renovation.info.data.repository.TodoRepository
import renovation.info.data.service.TodoService
import renovation.info.data.service.ValidatorService

private val log = KotlinLogging.logger { }

@Service
class TodoServiceImpl(
    @Autowired val todoRepository: TodoRepository,
    @Autowired val validatorService: ValidatorService,
) : TodoService {

    override fun getAll() = todoRepository.findAll()
        .also { log.info("find all todos") }

    override fun getById(id: Int) = todoRepository.findById(id).orElseThrow()
        .also { log.info("find single todo with id = ${id}") }

    override fun save(todosEntity: TodoEntity): TodoEntity {
        TODO("Not yet implemented")
    }
}
