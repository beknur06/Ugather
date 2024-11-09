package ugather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ugather.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
