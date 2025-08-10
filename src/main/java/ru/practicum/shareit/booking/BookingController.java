package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@RequestHeader(name = "X-Sharer-User-Id")
                                                            @Positive long userId,
                                                            @RequestBody @Valid BookingCreateDto bookingCreateDto) {
        log.info("Method launched (createBooking(long userId = {}, BookingCreateDto bookingCreateDto = {}))",
                userId, bookingCreateDto);
        BookingCreateDto newBookingCreateDto = new BookingCreateDto(bookingCreateDto.getStart(),
                bookingCreateDto.getEnd(), bookingCreateDto.getItemId(), userId, Status.WAITING);
        return new ResponseEntity<>(bookingService.createBooking(newBookingCreateDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> approveBooking(@RequestHeader(name = "X-Sharer-User-Id")
                                                             @Positive long owner,
                                                             @PathVariable @Positive long bookingId,
                                                             @RequestParam boolean approved) {
        log.info("Method launched (approveBooking(long owner = {}, long bookingId = {} boolean approved = {}))",
                owner, bookingId, approved);
        BookingApproveDto bookingApproveDto = new BookingApproveDto(bookingId, approved);
        return new ResponseEntity<>(bookingService.approveBooking(bookingApproveDto, owner), HttpStatus.OK);
    }
}