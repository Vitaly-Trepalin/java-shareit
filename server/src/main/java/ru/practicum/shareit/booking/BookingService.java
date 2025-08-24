package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingCreateDto bookingCreateDto);

    BookingResponseDto approveBooking(BookingApproveDto bookingApproveDto, long owner);

    BookingResponseDto getBooking(long user, long bookingId);

    List<BookingResponseDto> getBookingsForUser(State state, long user);

    List<BookingResponseDto> getBookingsForUserItems(State state, long user);
}