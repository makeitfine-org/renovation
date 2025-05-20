package renovation.neo4j.service.data.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import renovation.neo4j.service.data.node.Book;
import renovation.neo4j.service.web.dto.BookGetResponse;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "authorId", expression = "java(book.getAuthor().getId())")
    BookGetResponse toBookGetResponseMapper(Book book);
}
