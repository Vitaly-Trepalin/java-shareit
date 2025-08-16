package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateDto {
    @NotNull(message = "Должна быть дата начала бронирования")
    private LocalDateTime start;
    @NotNull(message = "Должна быть дата окончания бронирования")
    private LocalDateTime end;
    @NotNull(message = "Вещь не может быть null")
    private Long itemId;
    private Long bookerId;
    private Status status;
}