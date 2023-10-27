/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

package renovation.info.web.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.DELETE
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import renovation.info.data.model.TodoModel
import renovation.info.data.service.TodoService

//@CrossOrigin(
//    originPatterns = ["http://localhost:90*", "http://localhost:4200", "http://r"],
// todo: is it work? (take other port and it's still work)
////    originPatterns = ["*"],
//    methods = [GET, POST, DELETE]
//) //todo: cors with options
@RestController
@RequestMapping("/api/v1/info/todo")
class TodoController(
    @Autowired val todoService: TodoService
) {
    @GetMapping
    fun findAll() = todoService.getAll()

    @GetMapping("{id}")
    fun find(@PathVariable id: Int) = todoService.getById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody todoModel: TodoModel) = todoService.save(todoModel)

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) = todoService.delete(id)
}
