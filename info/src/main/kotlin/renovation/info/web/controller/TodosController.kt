/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.web.controller

import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import renovation.info.data.service.DetailsService
import renovation.info.data.service.TodosService

@CrossOrigin(originPatterns = ["http://localhost:80*", "http://r"])
@RestController
@RequestMapping("/api/v1/info/todo")
class TodosController(
    @Autowired val todosService: TodosService
) {
    @GetMapping
    fun get() = todosService.getAll()

    @GetMapping("{id}")
    fun find(@PathVariable id: Int)= todosService.getById(id)
}
