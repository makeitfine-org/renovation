package renovation.neo4j.service.web.dto;

public record BookGetResponse(
        Long id,
        String title,
        Integer year,
        Long authorId
) {
}
