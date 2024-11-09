package ugather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ugather.dto.BookingRequest;
import ugather.service.BookingService;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/{eventId}")
    public void bookEvent(@PathVariable Integer eventId, @RequestBody BookingRequest bookingRequest) {
        bookingService.bookEvent(eventId, bookingRequest.getEmail(), bookingRequest.getName(), bookingRequest.getSurname());
    }
}