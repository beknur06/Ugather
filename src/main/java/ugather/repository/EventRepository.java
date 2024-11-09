package ugather.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ugather.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findByAppUserId(Integer userId, Pageable pageable);
}
