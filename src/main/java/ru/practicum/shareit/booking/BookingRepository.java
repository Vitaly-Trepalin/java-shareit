package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Item;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findByItem(Item item);
}