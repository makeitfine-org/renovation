package renovation.neo4j.service.web.dto;

import java.time.LocalDate;

public record AuthorGetResponse(
        Long id,
        String firstName,
        String lastName,
        String middleName,
        LocalDate birthDate
) {
}
