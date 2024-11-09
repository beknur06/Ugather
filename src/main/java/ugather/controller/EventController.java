package ugather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ugather.dto.EventCreatRequestDto;
import ugather.dto.EventDto;
import ugather.service.EventService;

import java.util.Optional;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public Page<EventDto> getAllEvents(Pageable pageable) {
        return eventService.getAllEvents(pageable);
    }

    @GetMapping("/{id}")
    public Optional<EventDto> getEventById(@PathVariable Integer id) {
        return eventService.getEventById(id);
    }

    @PostMapping
    public void createEvent(@RequestBody EventCreatRequestDto eventDto) {
        eventService.createEvent(eventDto);
    }

    @PutMapping("/{id}")
    public void updateEvent(@PathVariable Integer id, @RequestBody EventDto eventDto) {
        eventService.updateEvent(id, eventDto);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
    }

    @GetMapping("/user/{userId}")
    public Page<EventDto> getEventsByUserId(
            @PathVariable Integer userId,Pageable pageable) {
        return eventService.getEventsByUserId(userId, pageable);
    }
}