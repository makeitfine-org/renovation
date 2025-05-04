package renovation.event.service.web.dto;

import java.time.LocalDate;

public record WorkEventRequest(
        String id,
        String title,
        String description,
        LocalDate endDate,
        double price,
        LocalDate payDate
) {
}
