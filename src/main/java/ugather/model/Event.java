package ugather.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private Integer capacity;
  private Integer registered;
  private String description;
  private String location;
  private EventType eventType;

  @ManyToOne
  private AppUser appUser;
}