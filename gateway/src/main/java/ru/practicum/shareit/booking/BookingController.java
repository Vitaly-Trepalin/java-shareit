package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(name = "X-Sharer-User-Id") @Positive long userId,
                                                @RequestBody @Valid BookingCreateDto bookingCreateDto) {
        log.info("Method launched (createBooking(long userId = {}, BookingCreateDto bookingCreateDto = {}))",
                userId, bookingCreateDto);
        return bookingClient.createBooking(userId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(name = "X-Sharer-User-Id") @Positive long owner,
                                                 @PathVariable @Positive long bookingId,
                                                 @RequestParam Boolean approved) {
        log.info("Method launched (approveBooking(long owner = {}, long bookingId = {} boolean approved = {}))",
                owner, bookingId, approved);
        return bookingClient.approveBooking(bookingId, owner, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(name = "X-Sharer-User-Id") @Positive long userId,
                                             @PathVariable @Positive long bookingId) {
        log.info("Method launched (getBooking(long userId = {}, long bookingId = {}))", userId, bookingId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsForUserItems(@RequestParam(defaultValue = "ALL") State state,
                                                          @RequestHeader(name = "X-Sharer-User-Id")
                                                          @Positive long owner) {
        log.info("Method launched (getBookingsForUserItems(State state = {}, long owner = {}))", state, owner);
        return bookingClient.getBookingsForUserItems(state, owner);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsForUser(@RequestParam(defaultValue = "ALL") State state,
                                                     @RequestHeader(name = "X-Sharer-User-Id")
                                                     @Positive long user) {
        log.info("Method launched (getBookingsForUser(State state = {}, long user = {}))", state, user);
        return bookingClient.getBookingsForUser(state, user);
    }
}