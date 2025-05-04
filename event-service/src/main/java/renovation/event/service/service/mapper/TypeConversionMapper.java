package renovation.event.service.service.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TypeConversionMapper {

    default String map(CharSequence value) {
        return value == null ? null : value.toString();
    }
}
