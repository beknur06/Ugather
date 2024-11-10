package ugather.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EventCreateRequest {
    private EventCreatRequestDto event;
    private MultipartFile file;
}