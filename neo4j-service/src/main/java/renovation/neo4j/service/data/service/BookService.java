/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.neo4j.service.data.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import renovation.neo4j.service.data.node.Book;
import renovation.neo4j.service.data.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository repository;

    public List<Book> findAllByYear(Integer year) {
        return repository.findAllByYearOrderByYearDesc(year);
    }

    public Optional<Book> findOneByTitle(String title) {
        return repository.findOneByTitle(title);
    }

    public List<Book> findBooksAfterYear(Integer year) {
        return repository.findByYearGreaterThan(year);
    }
}
