package ru.practicum.shareit.booking.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Booking {
    private Long id;
    private LocalDate start;
    private LocalDate end;
    private Long item;
    private Long booker;
    private Status status;
}