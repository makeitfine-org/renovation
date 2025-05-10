package renovation.event.service.web.dto;

import java.time.LocalDate;

public record WorkEventRequest(// todo: rename to WorkEventModel/DTO etc.; move from web
        String id,
        String title,
        String description,
        LocalDate endDate,
        double price,
        LocalDate payDate
) {
}
