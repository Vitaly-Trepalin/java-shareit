package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerOrderByStart(User booker);

    List<Booking> findAllByBookerAndStatusOrderByStartAsc(User booker, Status status);

    List<Booking> findAllByBookerAndStatusAndEndBeforeOrderByStartAsc(
            User booker, Status status, LocalDateTime currentTimeAndDate);

    List<Booking> findAllByBookerAndStatusAndStartAfterOrderByStartAsc(
            User booker, Status status, LocalDateTime currentTimeAndDate);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.booker = :booker
              AND b.status = :status
              AND b.start <= :currentTime
              AND b.end >= :currentTime
            ORDER BY b.start ASC
            """)
    List<Booking> findCurrentBookings(
            @Param("booker") User booker,
            @Param("status") Status status,
            @Param("currentTime") LocalDateTime currentTime);

    @Query("""
            SELECT b FROM Booking b
            JOIN b.item i
            WHERE i.owner = ?1
            ORDER BY b.start ASC
            """)
    List<Booking> findBookingsForAllUserItems(User owner);

    @Query("""
            SELECT b FROM Booking b
            JOIN b.item i
            WHERE i.owner = ?1 AND b.status = ?2
            ORDER BY b.start ASC
            """)
    List<Booking> findBookingsForAllUserItems(User owner, Status status);

    @Query("""
            SELECT b FROM Booking b
            JOIN b.item i
            WHERE i.owner = ?1 AND b.end < CURRENT_TIMESTAMP AND b.status = ?2
            ORDER BY b.start ASC
            """)
    List<Booking> findPastBookingsForAllUserItems(User owner, Status status);

    @Query("""
            SELECT b FROM Booking b
            JOIN b.item i
            WHERE i.owner = ?1 AND b.start <= CURRENT_TIMESTAMP AND b.end >= CURRENT_TIMESTAMP AND b.status = ?2
            ORDER BY b.start ASC
            """)
    List<Booking> findCurrentBookingsForAllUserItems(User owner, Status status);

    @Query("""
            SELECT b FROM Booking b
            JOIN b.item i
            WHERE i.owner = ?1 AND b.start > CURRENT_TIMESTAMP AND b.status = ?2
            ORDER BY b.start ASC
            """)
    List<Booking> findFutureBookingsForAllUserItems(User owner, Status status);

    @Query(value = """
            (SELECT * FROM bookings b
            WHERE b.item_id = :itemId AND b.end_date < CURRENT_TIMESTAMP
            ORDER BY b.start_date DESC
            LIMIT 1)
            UNION ALL
            (SELECT * FROM bookings b
            WHERE b.item_id = :itemId AND b.start_date > CURRENT_TIMESTAMP
            ORDER BY b.start_date ASC
            LIMIT 1)
            ORDER BY start_date
            """, nativeQuery = true)
    List<Booking> findLastAndNextBookingItem(@Param("itemId") long itemId);

    boolean existsByBookerAndItemAndStatusAndEndBefore(User booker, Item item, Status status, LocalDateTime end);
}