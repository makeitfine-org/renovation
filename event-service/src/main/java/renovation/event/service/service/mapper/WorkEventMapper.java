package renovation.event.service.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.web.dto.WorkEventRequest;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface WorkEventMapper {
    WorkEventMapper INSTANCE = Mappers.getMapper(WorkEventMapper.class);

    @Mapping(source = "endDate", target = "endDate", qualifiedByName = "localDateToString")
    @Mapping(source = "payDate", target = "payDate", qualifiedByName = "localDateToString")
    WorkEvent toAvroWorkEvent(WorkEventRequest workEventRequest);

    @Named("localDateToString")
    static String localDateToString(LocalDate localDate) {
        return localDate != null ? localDate.toString() : null;
    }
}
