package ugather.dto;

import lombok.Data;
import ugather.model.EventType;

import java.util.Collection;

@Data
public class EventFilterRequestBody {
    private Collection<EventType> eventTypes;
}
