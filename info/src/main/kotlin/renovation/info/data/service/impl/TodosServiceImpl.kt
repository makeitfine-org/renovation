/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.data.service.impl

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import renovation.info.data.model.TodosEntity
import renovation.info.data.repository.TodosRepository
import renovation.info.data.service.TodosService
import renovation.info.data.service.ValidatorService

private val log = KotlinLogging.logger { }

@Service
class TodosServiceImpl(
    @Autowired val todosRepository: TodosRepository,
    @Autowired val validatorService: ValidatorService,
) : TodosService {

    override fun getAll() = todosRepository.findAll()
        .also { log.info("find all todos") }

    override fun getById(id: Int) = todosRepository.findById(id).orElseThrow()
        .also { log.info("find single todo with id = ${id}") }

    override fun save(todosEntity: TodosEntity): TodosEntity {
        TODO("Not yet implemented")
    }
}
