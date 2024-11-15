package ugather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ugather.dto.EventCreatRequestDto;
import ugather.dto.EventCreateRequest;
import ugather.dto.EventDto;
import ugather.dto.EventFilterRequestBody;
import ugather.service.EventService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Tag(name = "Event Controller", description = "APIs for managing events")
public class EventController {
    private final EventService eventService;

    @Operation(summary = "Get all events")
    @GetMapping
    public Page<EventDto> getAllEvents(Pageable pageable) {
        return eventService.getAllEvents(pageable);
    }

    @Operation(summary = "Get event by ID")
    @GetMapping("/{id}")
    public Optional<EventDto> getEventById(@PathVariable Integer id) {
        return eventService.getEventById(id);
    }

    @Operation(summary = "Filter events")
    @PostMapping("/filter")
    public Page<EventDto> filterEvents(@RequestBody EventFilterRequestBody filterRequestBody, Pageable pageable) {
        return eventService.filterEvents(filterRequestBody, pageable);
    }

    @Operation(summary = "Search events")
    @GetMapping("/search")
    public Page<EventDto> searchEvents(@RequestParam String keyword, Pageable pageable) {
        return eventService.searchEvents(keyword, pageable);
    }

    @Operation(summary = "Get trending events")
    @GetMapping("/trending")
    public List<EventDto> getTrendingEvents() {
        return eventService.getTrendingEvents();
    }

    @Operation(summary = "Get upcoming events")
    @GetMapping("/upcoming")
    public List<EventDto> getUpcomingEvents() {
        return eventService.getUpcomingEvents();
    }

    @Operation(summary = "Get today's events")
    @GetMapping("/today")
    public List<EventDto> getTodayEvents() {
        return eventService.getTodayEvents();
    }

    @Operation(summary = "Create a new event")
    @PostMapping
    public void createEvent(@RequestBody EventCreateRequest eventCreateRequest) throws IOException {
        eventService.createEvent(eventCreateRequest.getEvent(), eventCreateRequest.getFile());
    }

    @Operation(summary = "Update an event")
    @PutMapping("/{id}")
    public void updateEvent(@PathVariable Integer id, @RequestBody EventDto eventDto) {
        eventService.updateEvent(id, eventDto);
    }

    @Operation(summary = "Delete an event")
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
    }

    @Operation(summary = "Get events by user ID")
    @GetMapping("/user/{userId}")
    public Page<EventDto> getEventsByUserId(@PathVariable Integer userId, Pageable pageable) {
        return eventService.getEventsByUserId(userId, pageable);
    }


}