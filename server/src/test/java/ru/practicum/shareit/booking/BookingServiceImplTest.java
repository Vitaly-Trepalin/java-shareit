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
import ru.practicum.shareit.exception.NotFoundException;
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
        webEnvironment = SpringBootTest.WebEnvironment.NONE
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
    void testCreateBookingWithNonExistentUser() {
        BookingCreateDto invalidDto = new BookingCreateDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item.getId(),
                50L,
                Status.WAITING
        );

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(invalidDto));
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
    void testCreateBookingWithNonExistentItem() {
        BookingCreateDto invalidDto = new BookingCreateDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                50L,
                booker.getId(),
                Status.WAITING
        );

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(invalidDto));
    }

    @Test
    void testCreateBookingWithUnavailableItem() {
        item.setAvailable(false);
        itemRepository.save(item);

        assertThrows(IllegalStateException.class, () -> bookingService.createBooking(bookingCreateDto));
    }

    @Test
    void testApproveBookingWithRejected() {
        BookingApproveDto approveDto = new BookingApproveDto(booking.getId(), false);

        BookingResponseDto result = bookingService.approveBooking(approveDto, owner.getId());

        assertNotNull(result);
        assertEquals(Status.REJECTED, result.getStatus());

        Optional<Booking> updatedBooking = bookingRepository.findById(booking.getId());
        assertTrue(updatedBooking.isPresent());
        assertEquals(Status.REJECTED, updatedBooking.get().getStatus());
    }

    @Test
    void testApproveBookingWithNonExistentBooking() {
        BookingApproveDto approveDto = new BookingApproveDto(50L, true);

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(approveDto, owner.getId()));
    }

    @Test
    void testApproveBookingByNonOwner() {
        BookingApproveDto approveDto = new BookingApproveDto(booking.getId(), true);

        assertThrows(org.springframework.web.client.HttpClientErrorException.class,
                () -> bookingService.approveBooking(approveDto, booker.getId())); // booker не владелец
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
    void testGetBookingWithNonExistentBooking() {
        assertThrows(NotFoundException.class, () -> bookingService.getBooking(booker.getId(), 50L));
    }

    @Test
    void testGetBookingsForUserItems() {
        List<BookingResponseDto> result = bookingService.getBookingsForUserItems(State.ALL, owner.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    void testGetBookingsForUserWithStateWaiting() {
        List<BookingResponseDto> result = bookingService.getBookingsForUser(State.WAITING, booker.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Status.WAITING, result.get(0).getStatus());
    }

    @Test
    void testGetBookingsForUserWithStateRejected() {
        Booking rejectedBooking = new Booking();
        rejectedBooking.setStart(LocalDateTime.now().plusDays(5));
        rejectedBooking.setEnd(LocalDateTime.now().plusDays(6));
        rejectedBooking.setItem(item);
        rejectedBooking.setBooker(booker);
        rejectedBooking.setStatus(Status.REJECTED);
        bookingRepository.save(rejectedBooking);

        List<BookingResponseDto> result = bookingService.getBookingsForUser(State.REJECTED, booker.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Status.REJECTED, result.get(0).getStatus());
    }

    @Test
    void testGetBookingsForUserWithStatePast() {
        Booking pastBooking = new Booking();
        pastBooking.setStart(LocalDateTime.now().minusDays(2));
        pastBooking.setEnd(LocalDateTime.now().minusDays(1));
        pastBooking.setItem(item);
        pastBooking.setBooker(booker);
        pastBooking.setStatus(Status.APPROVED);
        bookingRepository.save(pastBooking);

        List<BookingResponseDto> result = bookingService.getBookingsForUser(State.PAST, booker.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getEnd().isBefore(LocalDateTime.now()));
    }

    @Test
    void testGetBookingsForUserWithNonExistentUser() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingsForUser(State.ALL, 50L));
    }

    @Test
    void testGetBookingsForUserItemsWithNonExistentUser() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingsForUserItems(State.ALL, 999L));
    }
}