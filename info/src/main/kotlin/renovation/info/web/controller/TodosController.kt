/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.web.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import renovation.info.data.service.TodoService

@CrossOrigin(originPatterns = ["http://localhost:80*", "http://r"])
@RestController
@RequestMapping("/api/v1/info/todo")
class TodosController(
    @Autowired val todoService: TodoService
) {
    @GetMapping
    fun get() = todoService.getAll()

    @GetMapping("{id}")
    fun find(@PathVariable id: Int)= todoService.getById(id)
}
