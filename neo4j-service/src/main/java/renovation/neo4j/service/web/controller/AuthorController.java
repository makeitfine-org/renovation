/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.neo4j.service.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import renovation.neo4j.service.data.node.Author;
import renovation.neo4j.service.data.service.AuthorService;
import renovation.neo4j.service.web.Route;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(Route.AUTHOR)
@AllArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Author create(@RequestBody Author author) {
        return service.save(author);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Author> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Author> getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
