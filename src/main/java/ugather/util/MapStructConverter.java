package ugather.util;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;
import ugather.dto.EventDto;
import ugather.model.Event;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MapStructConverter {
    MapStructConverter MAPPER = Mappers.getMapper(MapStructConverter.class);

    EventDto eventToEventDto(Event event);
    Event eventDtoToEvent(EventDto eventDto);
}