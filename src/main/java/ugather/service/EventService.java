package ugather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ugather.dto.EventCreatRequestDto;
import ugather.dto.EventDto;
import ugather.dto.EventFilterRequestBody;
import ugather.model.AppUser;
import ugather.model.Event;
import ugather.repository.AppUserRepository;
import ugather.repository.EventRepository;
import ugather.util.MapStructConverter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    MapStructConverter mapStructConverter;

    public Page<EventDto> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable).map(mapStructConverter::eventToEventDto);
    }

    public Optional<EventDto> getEventById(Integer id) {
        return eventRepository.findById(id).map(mapStructConverter::eventToEventDto);
    }

    public Page<EventDto> getTodayEvents(Pageable pageable) {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        return eventRepository.findTodayEvents(startOfDay, endOfDay, pageable).map(mapStructConverter::eventToEventDto);
    }

    public Page<EventDto> getTrendingEvents(Pageable pageable) {
        return eventRepository.findTrendingEvents(pageable).map(mapStructConverter::eventToEventDto);
    }

    public Page<EventDto> getUpcomingEvents(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findUpcomingEventsSorted(now, pageable).map(mapStructConverter::eventToEventDto);
    }

    public Page<EventDto> filterEvents(EventFilterRequestBody filterRequestBody, Pageable pageable) {
        return eventRepository.findByEventTypeIsIn(filterRequestBody.getEventTypes(), pageable)
                .map(mapStructConverter::eventToEventDto);
    }

    public void createEvent(EventCreatRequestDto eventCreatRequestDto) {
        EventDto eventDto = new EventDto();

        eventDto.setStartDateTime(eventCreatRequestDto.getStartDateTime());
        eventDto.setEndDateTime(eventCreatRequestDto.getEndDateTime());
        eventDto.setCapacity(eventCreatRequestDto.getCapacity());
        eventDto.setRegistered(0);
        eventDto.setDescription(eventCreatRequestDto.getDescription());
        eventDto.setLocation(eventCreatRequestDto.getLocation());
        eventDto.setEventType(eventCreatRequestDto.getEventType());

        Event event = mapStructConverter.eventDtoToEvent(eventDto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        event.setAppUser(appUser);

        eventRepository.save(event);
    }

    public void updateEvent(Integer id, EventDto eventDto) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));

        if (eventDto.getStartDateTime() != null) {
            event.setStartDateTime(eventDto.getStartDateTime());
        }
        if (eventDto.getEndDateTime() != null) {
            event.setEndDateTime(eventDto.getEndDateTime());
        }
        if (eventDto.getCapacity() != null) {
            event.setCapacity(eventDto.getCapacity());
        }
        if (eventDto.getRegistered() != null) {
            event.setRegistered(eventDto.getRegistered());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(eventDto.getLocation());
        }
        if (eventDto.getEventType() != null) {
            event.setEventType(eventDto.getEventType());
        }

        eventRepository.save(event);
    }

    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }

    @Scheduled(cron = "0 */1 * * * ?") // Runs every 1 minutes
    public void deleteFinishedEvents() {
        LocalDateTime now = LocalDateTime.now();
        eventRepository.deleteFinishedEvents(now);
    }

    public Page<EventDto> getEventsByUserId(Integer userId, Pageable pageable) {
        return eventRepository.findByAppUserId(userId, pageable).map(mapStructConverter::eventToEventDto);
    }
}