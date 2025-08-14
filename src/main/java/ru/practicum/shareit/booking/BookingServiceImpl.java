package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingCreateDto bookingCreateDto) {
        if (!bookingCreateDto.getEnd().isAfter(bookingCreateDto.getStart())) {
            throw new IllegalArgumentException("Время окончания бронирования не может равняться или быть раньше " +
                    "времени начала");
        }

        User user = userRepository.findById(bookingCreateDto.getBookerId())
                .orElseThrow(() -> new NotFoundException(String.format("Нет пользователя с id = %d",
                        bookingCreateDto.getBookerId())));

        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("Нет вещи с id = %d",
                        bookingCreateDto.getItemId())));

        if (!item.isAvailable()) {
            throw new IllegalStateException(String.format("Вещь c id = %d недоступна для бронирования", item.getId()));
        }

        Booking booking = bookingRepository.save(BookingMapper.mapToBooking(bookingCreateDto, item, user));
        return BookingMapper.mapToBookingResponseDto(booking);
    }

    @Override
    @Transactional
    public BookingResponseDto approveBooking(BookingApproveDto bookingApproveDto, long owner) {
        Booking booking = bookingRepository.findById(bookingApproveDto.getBookingId())
                .orElseThrow(() -> new NotFoundException(String.format("Нет бронирования с id = %d",
                        bookingApproveDto.getBookingId())));

        if (!Objects.equals(booking.getItem().getOwner().getId(), owner)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, String.format("Пользователь с id = %d " +
                            "не является владельцем вещи с id = %d",
                    owner, bookingApproveDto.getBookingId()));
        }

        if (bookingApproveDto.getApproved()) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.mapToBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto getBooking(long user, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Нет бронирования с id = %d", bookingId)));

        boolean isBooker = booking.getBooker().getId() == user;
        boolean isOwner = booking.getItem().getOwner().getId() == user;

        if (!isBooker && !isOwner) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN,
                    String.format("Пользователь с id = %d не может получить информацию о бронировании", user));
        }
        return BookingMapper.mapToBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookingsForUser(State state, long userId) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Нет пользователя с id = %d", userId)));

        LocalDateTime currentTimeAndDate = LocalDateTime.now();
        List<Booking> bookings = switch (state) {
            case State.REJECTED -> bookingRepository.findAllByBookerAndStatusOrderByStartAsc(booker, Status.REJECTED);
            case State.WAITING -> bookingRepository.findAllByBookerAndStatusOrderByStartAsc(booker, Status.WAITING);
            case State.PAST -> bookingRepository.findAllByBookerAndStatusAndEndBeforeOrderByStartAsc(booker,
                    Status.APPROVED, currentTimeAndDate);
            case State.FUTURE -> bookingRepository.findAllByBookerAndStatusAndStartAfterOrderByStartAsc(
                    booker, Status.APPROVED, currentTimeAndDate);
            case State.CURRENT -> bookingRepository.findCurrentBookings(booker, Status.APPROVED, currentTimeAndDate);
            default -> bookingRepository.findAllByBookerOrderByStart(booker);
        };
        return bookings.stream()
                .map(BookingMapper::mapToBookingResponseDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsForUserItems(State state, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Нет пользователя с id = %d", userId)));

        Collection<Booking> bookings = switch (state) {
            case State.REJECTED -> bookingRepository.findBookingsForAllUserItems(owner, Status.REJECTED);
            case State.WAITING -> bookingRepository.findBookingsForAllUserItems(owner, Status.WAITING);
            case State.PAST -> bookingRepository.findPastBookingsForAllUserItems(owner, Status.APPROVED);
            case State.FUTURE -> bookingRepository.findFutureBookingsForAllUserItems(owner, Status.APPROVED);
            case State.CURRENT -> bookingRepository.findCurrentBookingsForAllUserItems(owner, Status.APPROVED);
            default -> bookingRepository.findBookingsForAllUserItems(owner);
        };
        return bookings.stream()
                .map(BookingMapper::mapToBookingResponseDto)
                .toList();
    }
}