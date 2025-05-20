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
import renovation.neo4j.service.data.service.mapper.AuthorMapper;
import renovation.neo4j.service.web.Route;
import renovation.neo4j.service.web.dto.AuthorGetResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(Route.AUTHOR)
@AllArgsConstructor
public class AuthorController {

    private final AuthorService service;

    private final AuthorMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorGetResponse create(@RequestBody Author author) {
        var saved = service.save(author);
        return mapper.toAuthorGetResponseMapper(saved);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AuthorGetResponse> getAll() {
        return service.findAll().stream()
                .map(mapper::toAuthorGetResponseMapper)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<AuthorGetResponse> getById(@PathVariable Long id) {
        return service.findById(id).map(mapper::toAuthorGetResponseMapper);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping("/find/before/year/{year}")
    @ResponseStatus(HttpStatus.OK)
    public List<AuthorGetResponse> findAuthorsWithBooksBeforeYear(@PathVariable Integer year) {
        return service.findAuthorsWithBooksBeforeYear(year).stream()
                .map(mapper::toAuthorGetResponseMapper)
                .toList();
    }
}
