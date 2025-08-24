package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {"spring.datasource.driverClassName=org.postgresql.Driver",
                "spring.datasource.url=jdbc:postgresql://localhost:5433/test",
                "spring.datasource.username=postgres",
                "spring.datasource.password=12345"}
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplTest {
    private final BookingServiceImpl bookingService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item item;
    private Booking booking;
    private BookingCreateDto bookingCreateDto;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@ya.ru");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@ya.ru");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);
        booking = bookingRepository.save(booking);

        bookingCreateDto = new BookingCreateDto(
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(4),
                item.getId(),
                booker.getId(),
                Status.WAITING
        );
    }

    @Test
    void testCreateBooking() {
        BookingResponseDto result = bookingService.createBooking(bookingCreateDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(bookingCreateDto.getStart(), result.getStart());
        assertEquals(bookingCreateDto.getEnd(), result.getEnd());
        assertEquals(Status.WAITING, result.getStatus());
        assertEquals(booker.getId(), result.getBooker().getId());
        assertEquals(item.getId(), result.getItem().getId());

        Optional<Booking> savedBooking = bookingRepository.findById(result.getId());
        assertTrue(savedBooking.isPresent());
        assertEquals(bookingCreateDto.getStart(), savedBooking.get().getStart());
    }

    @Test
    void testApproveBooking() {
        BookingApproveDto approveDto = new BookingApproveDto(booking.getId(), true);

        BookingResponseDto result = bookingService.approveBooking(approveDto, owner.getId());

        assertNotNull(result);
        assertEquals(Status.APPROVED, result.getStatus());

        Optional<Booking> updatedBooking = bookingRepository.findById(booking.getId());
        assertTrue(updatedBooking.isPresent());
        assertEquals(Status.APPROVED, updatedBooking.get().getStatus());
    }

    @Test
    void testGetBooking() {
        BookingResponseDto result = bookingService.getBooking(booker.getId(), booking.getId());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
    }

    @Test
    void testGetBookingsForUser() {
        Booking anotherBooking = new Booking();
        anotherBooking.setStart(LocalDateTime.now().plusDays(5));
        anotherBooking.setEnd(LocalDateTime.now().plusDays(6));
        anotherBooking.setItem(item);
        anotherBooking.setBooker(booker);
        anotherBooking.setStatus(Status.WAITING);
        bookingRepository.save(anotherBooking);

        List<BookingResponseDto> result = bookingService.getBookingsForUser(State.ALL, booker.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetBookingsForUserItems() {
        List<BookingResponseDto> result = bookingService.getBookingsForUserItems(State.ALL, owner.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
    }
}