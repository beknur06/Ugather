package ugather.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppUserDto {
  private Integer id;
  private String username;
  private String email;
}
