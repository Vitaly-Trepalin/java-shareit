package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

public interface BookingService {
    BookingResponseDto createBooking(BookingCreateDto bookingCreateDto);

    BookingResponseDto approveBooking(BookingApproveDto bookingApproveDto, long owner);
}