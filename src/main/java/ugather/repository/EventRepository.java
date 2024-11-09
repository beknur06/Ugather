package ugather.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ugather.model.Event;
import ugather.model.EventType;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findByAppUserId(Integer userId, Pageable pageable);
    Page<Event> findByEventTypeIsIn(Collection<EventType> eventType, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.startDateTime BETWEEN :startOfDay AND :endOfDay")
    List<Event> findTodayEvents(LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT e FROM Event e ORDER BY e.registered DESC")
    List<Event> findTrendingEvents();

    @Query("SELECT e FROM Event e WHERE e.startDateTime > :now ORDER BY e.startDateTime ASC")
    List<Event> findUpcomingEventsSorted(LocalDateTime now);

    @Query("SELECT e FROM Event e WHERE e.endDateTime < :now")
    List<Event> findFinishedEvents(LocalDateTime now);

    @Query("SELECT e FROM Event e WHERE e.title LIKE %:keyword%")
    Page<Event> searchEventsByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Event e WHERE e.endDateTime < :now")
    void deleteFinishedEvents(LocalDateTime now);
}
