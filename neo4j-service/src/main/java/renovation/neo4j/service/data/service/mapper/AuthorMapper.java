package renovation.neo4j.service.data.service.mapper;

import org.mapstruct.Mapper;
import renovation.neo4j.service.data.node.Author;
import renovation.neo4j.service.web.dto.AuthorGetResponse;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorGetResponse toAuthorGetResponseMapper(Author author);
}
