package renovation.event.service.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;
import renovation.event.service.web.dto.WorkEventRequest;

import java.time.LocalDate;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface WorkEventMapper {

    @Named("localDateToString")
    static String localDateToString(LocalDate localDate) {
        return localDate != null ? localDate.toString() : null;
    }

    @Mapping(source = "endDate", target = "endDate", qualifiedByName = "localDateToString")
    @Mapping(source = "payDate", target = "payDate", qualifiedByName = "localDateToString")
    WorkEvent toAvroWorkEvent(WorkEventRequest workEventRequest);

    default WorkEventKey toAvroWorkEventKey(WorkEventRequest workEventRequest) {
        return new WorkEventKey(UUID.randomUUID().toString());
    }
}
