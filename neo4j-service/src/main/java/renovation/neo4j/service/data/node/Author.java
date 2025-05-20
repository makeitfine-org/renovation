/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.neo4j.service.data.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@Node("Author")
public class Author {

    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private String middleName;

    private LocalDate birthDate;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Relationship(type = "WRITTEN_BY", direction = Relationship.Direction.INCOMING)
    private List<Book> books;
}
