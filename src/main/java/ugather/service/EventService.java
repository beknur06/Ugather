package ugather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ugather.dto.EventCreatRequestDto;
import ugather.dto.EventDto;
import ugather.dto.EventFilterRequestBody;
import ugather.model.AppUser;
import ugather.model.Event;
import ugather.repository.AppUserRepository;
import ugather.repository.EventRepository;
import ugather.util.MapStructConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    MapStructConverter mapStructConverter;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Page<EventDto> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable).map(mapStructConverter::eventToEventDto);
    }

    public Optional<EventDto> getEventById(Integer id) {
        return eventRepository.findById(id).map(mapStructConverter::eventToEventDto);
    }

    public List<EventDto> getTodayEvents() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        return eventRepository.findTodayEvents(startOfDay, endOfDay).stream().map(mapStructConverter::eventToEventDto).toList();
    }

    public List<EventDto> getTrendingEvents() {
        return eventRepository.findTrendingEvents().stream().map(mapStructConverter::eventToEventDto).toList();
    }

    public List<EventDto> getUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findUpcomingEventsSorted(now).stream().map(mapStructConverter::eventToEventDto).toList();
    }

    public Page<EventDto> filterEvents(EventFilterRequestBody filterRequestBody, Pageable pageable) {
        return eventRepository.findByEventTypeIsIn(filterRequestBody.getEventTypes(), pageable)
                .map(mapStructConverter::eventToEventDto);
    }

    public Page<EventDto> searchEvents(String keyword, Pageable pageable) {
        return eventRepository.searchEventsByKeyword(keyword, pageable).map(mapStructConverter::eventToEventDto);
    }

    public void createEvent(EventCreatRequestDto eventCreatRequestDto, MultipartFile file) throws IOException {
        EventDto eventDto = getEventDto(eventCreatRequestDto);

        Event event = mapStructConverter.eventDtoToEvent(eventDto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        event.setAppUser(appUser);

        if (file != null && !file.isEmpty()) {
            String fileUrl = saveFileLocally(file);
            event.setFileUrl(fileUrl);
        }

        eventRepository.save(event);
    }

    private static EventDto getEventDto(EventCreatRequestDto eventCreatRequestDto) {
        EventDto eventDto = new EventDto();

        eventDto.setTitle(eventCreatRequestDto.getTitle());
        eventDto.setStartDateTime(eventCreatRequestDto.getStartDateTime());
        eventDto.setEndDateTime(eventCreatRequestDto.getEndDateTime());
        eventDto.setCapacity(eventCreatRequestDto.getCapacity());
        eventDto.setRegistered(0);
        eventDto.setDescription(eventCreatRequestDto.getDescription());
        eventDto.setLocation(eventCreatRequestDto.getLocation());
        eventDto.setEventType(eventCreatRequestDto.getEventType());
        return eventDto;
    }

    private String saveFileLocally(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath);
        return filePath.toString();
    }

    public void updateEvent(Integer id, EventDto eventDto) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));

        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
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