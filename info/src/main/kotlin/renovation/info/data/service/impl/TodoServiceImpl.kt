/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.service.impl

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import renovation.info.data.model.TodoModel
import renovation.info.data.repository.TodoRepository
import renovation.info.data.service.TodoService
import renovation.info.data.service.ValidatorService
import renovation.info.data.util.Helper

private val log = KotlinLogging.logger { }

@Suppress("TooGenericExceptionThrown")
@Service
class TodoServiceImpl(
    @Autowired val todoRepository: TodoRepository,
    @Autowired val validatorService: ValidatorService,
) : TodoService {

    override fun getAll() = todoRepository.findAll()
        .map(Helper::convert)
        .toList()
        .also { log.info("find all todos") }

    override fun getById(id: Int) = todoRepository.findById(id)
        .map(Helper::convert)
        .orElseThrow()
        .also { log.info("find single todo with id = $id") }

    override fun save(todoModel: TodoModel) = todoRepository.save(
        validatorService.validate(
            Helper.convert(todoModel)
        )
    ).let { log.info("create single todo with id = $todoModel") }

    @Throws(RuntimeException::class)
    override fun update(todoModel: TodoModel) {
        todoRepository.findById(todoModel.id).orElseThrow()

        todoRepository.save(
            validatorService.validate(
                Helper.convert(todoModel)
            )
        )

        log.info("update single todo: $todoModel")
    }

    @Throws(RuntimeException::class)
    override fun delete(id: Int) {
        if (!todoRepository.existsById(id)) {
            throw RuntimeException("Not found todo with id = $id")
        }
        todoRepository.deleteById(id)

        log.info("delete single todo with id = $id")
    }
}
