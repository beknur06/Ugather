package ugather.dto;

import lombok.Data;
import ugather.model.EventType;

import java.time.LocalDateTime;

@Data
public class EventCreatRequestDto {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer capacity;
    private String description;
    private String location;
    private EventType eventType;
}
