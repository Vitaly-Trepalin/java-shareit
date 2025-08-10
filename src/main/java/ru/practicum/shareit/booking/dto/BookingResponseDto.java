package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private User booker;
    private Item item;
}