package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                            @RequestBody BookingCreateDto bookingCreateDto) {
        log.info("Method launched (createBooking(long userId = {}, BookingCreateDto bookingCreateDto = {}))",
                userId, bookingCreateDto);
        BookingCreateDto newBookingCreateDto = new BookingCreateDto(bookingCreateDto.getStart(),
                bookingCreateDto.getEnd(), bookingCreateDto.getItemId(), userId, Status.WAITING);
        return bookingService.createBooking(newBookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader(name = "X-Sharer-User-Id") long owner,
                                             @PathVariable long bookingId, @RequestParam boolean approved) {
        log.info("Method launched (approveBooking(long owner = {}, long bookingId = {} boolean approved = {}))",
                owner, bookingId, approved);
        BookingApproveDto bookingApproveDto = new BookingApproveDto(bookingId, approved);
        return bookingService.approveBooking(bookingApproveDto, owner);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader(name = "X-Sharer-User-Id") long user,
                                         @PathVariable long bookingId) {
        log.info("Method launched (getBooking(long user = {}, long bookingId = {}))", user, bookingId);
        return bookingService.getBooking(user, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookingsForUser(@RequestParam(defaultValue = "ALL") State state,
                                                       @RequestHeader(name = "X-Sharer-User-Id") long user) {
        log.info("Method launched (getBookingsForUser(State state = {}, long user = {}))", state, user);
        return bookingService.getBookingsForUser(state, user);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsForUserItems(@RequestParam(defaultValue = "ALL") State state,
                                                            @RequestHeader(name = "X-Sharer-User-Id") long owner) {
        log.info("Method launched (getBookingsForUserItems(State state = {}, long owner = {}))", state, owner);
        return bookingService.getBookingsForUserItems(state, owner);
    }
}