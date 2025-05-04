package renovation.event.service.service.mapper;

import org.mapstruct.Mapper;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.web.dto.WorkEventRequest;

@Mapper(componentModel = "spring", uses = TypeConversionMapper.class)
public interface WorkEventRequestMapper {

    WorkEventRequest toWorkEventRequest(WorkEvent workEvent);
}
