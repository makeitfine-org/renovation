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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@Node("Book")
public class Book {
    @Id
    private Long id;

    private String title;
    private Integer year;

    @Relationship(type = "WRITTEN_BY", direction = Relationship.Direction.OUTGOING)
    private Author author;
}
