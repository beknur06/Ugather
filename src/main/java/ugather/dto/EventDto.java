package ugather.dto;

import lombok.Data;
import ugather.model.EventType;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private Integer id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer capacity;
    private Integer registered;
    private String description;
    private String location;
    private EventType eventType;
    private AppUserDto appUser;
}
