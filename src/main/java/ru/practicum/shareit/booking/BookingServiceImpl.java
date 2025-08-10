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
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

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
        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("Нет вещи с id = %d",
                        bookingCreateDto.getItemId())));
        if (!item.isAvailable()) {
            throw new IllegalStateException("Вещь недоступна");
        }
        User user = userRepository.findById(bookingCreateDto.getBookerId())
                .orElseThrow(() -> new NotFoundException(String.format("Нет пользователя с id = %d",
                        bookingCreateDto.getBookerId())));
        Booking booking = bookingRepository.save(BookingMapper.mapToBooking(bookingCreateDto, item, user));
        return BookingMapper.mapToBookingResponseDto(booking);
    }

    @Override
    @Transactional
    public BookingResponseDto approveBooking(BookingApproveDto bookingApproveDto, long owner) {
//        if (bookingApproveDto.getBookingId() == null) {
//            Collection<Item> items = itemRepository.findAllByOwnerIdAndStatusTrue(owner);
//            Collection<Booking> bookings = bookingRepository.findAllByItem(item);
//        }
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


}
