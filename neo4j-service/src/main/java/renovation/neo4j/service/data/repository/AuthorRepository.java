/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.neo4j.service.data.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import renovation.neo4j.service.data.node.Author;

import java.util.List;

@Repository
public interface AuthorRepository extends Neo4jRepository<Author, Long> {

    @Query("""
                MATCH (a:Author)<-[:WRITTEN_BY]-(b:Book)
                WHERE b.year < $year
                RETURN DISTINCT a
            """)
    List<Author> findAuthorsWithBooksBeforeYear(@Param("year") Integer year);
}
