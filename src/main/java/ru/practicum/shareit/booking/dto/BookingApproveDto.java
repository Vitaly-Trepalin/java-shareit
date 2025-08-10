package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

@Data
@AllArgsConstructor
public class BookingApproveDto {
    private Long bookingId;
    private Boolean approved;
}
