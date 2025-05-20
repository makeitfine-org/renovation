/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.neo4j.service.data.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import renovation.neo4j.service.data.node.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends Neo4jRepository<Book, Long> {
    Optional<Book> findOneByTitle(String title);

    List<Book> findAllByYearOrderByYearDesc(Integer year);

    List<Book> findByYearGreaterThan(Integer year);
}
