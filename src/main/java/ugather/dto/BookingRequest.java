package ugather.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private String email;
    private String name;
    private String surname;
}