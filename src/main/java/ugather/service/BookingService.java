package ugather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ugather.model.Booking;
import ugather.model.Event;
import ugather.repository.BookingRepository;
import ugather.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;
    private final JavaMailSender mailSender;

    public void bookEvent(Integer eventId, String email, String name, String surname) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        if (event.getRegistered() >= event.getCapacity()) {
            throw new RuntimeException("No available spots");
        }

        Booking booking = new Booking();
        booking.setEvent(event);
        booking.setEmail(email);
        booking.setName(name);
        booking.setSurname(surname);
        bookingRepository.save(booking);

        event.setRegistered(event.getRegistered() + 1);
        eventRepository.save(event);

        sendConfirmationEmail(email, event);
    }

    private void sendConfirmationEmail(String email, Event event) {
        if (email != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Event Booking Confirmation");
            message.setText("You have successfully booked a spot for the event: " + event.getDescription());
            mailSender.send(message);
        } else {
            // Handle the null case
            throw new RuntimeException("Email or event is null");
        }
    }
}