package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.Collection;

public interface BookingService {
    BookingResponseDto createBooking(BookingCreateDto bookingCreateDto);

    BookingResponseDto approveBooking(BookingApproveDto bookingApproveDto, long owner);

    BookingResponseDto getBooking(long user, long bookingId);

    Collection<BookingResponseDto> getBookingsForUser(String state, long user);

    Collection<BookingResponseDto> getBookingsForUserItems(String state, long user);
}