/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.neo4j.service.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import renovation.neo4j.service.data.service.BookService;
import renovation.neo4j.service.data.service.mapper.BookMapper;
import renovation.neo4j.service.web.Route;
import renovation.neo4j.service.web.dto.BookGetResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(Route.BOOK)
@AllArgsConstructor
public class BookController {

    private final BookService service;

    private final BookMapper mapper;

    @GetMapping("/find/by/year/{year}")
    @ResponseStatus(HttpStatus.OK)
    public List<BookGetResponse> findAllByYear(@PathVariable Integer year) {
        return service.findAllByYear(year).stream()
                .map(mapper::toBookGetResponseMapper)
                .toList();
    }

    @GetMapping("/find/by/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<BookGetResponse> findOneByTitle(@PathVariable String title) {
        return service.findOneByTitle(title).map(mapper::toBookGetResponseMapper);
    }
}
