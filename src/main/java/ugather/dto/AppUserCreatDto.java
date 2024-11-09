package ugather.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppUserCreatDto {
  private String username;
  private String email;
  private String password;
}
