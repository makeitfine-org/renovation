/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.neo4j.service.data.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import renovation.neo4j.service.data.node.Author;
import renovation.neo4j.service.data.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository repository;

    public Author save(Author author) {
        return repository.save(author);
    }

    public List<Author> findAll() {
        return repository.findAll(Sort.by("id").ascending());
    }

    public Optional<Author> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
