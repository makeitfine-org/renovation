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
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDate;

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
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;
    private String middleName;

    private LocalDate birthDate;

    public Author(LocalDate birthDate, String middleName, String lastName, String firstName) {
        this.birthDate = birthDate;
        this.middleName = middleName;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public Author(String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }
}
