package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateCommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseWithDatesAndCommentsDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemResponseWithDatesAndCommentsDto findByIdItem(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Нет вещи с id = %d", itemId)));

        List<CommentResponseDto> comments = commentRepository.findAllCommentByItem(item).stream()
                .map(CommentMapper::mapToCommentResponseDto).toList();

        Collection<Booking> bookings = bookingRepository.findLastAndNextBookingItem(itemId);
        List<LocalDateTime> lastBooking = null;
        List<LocalDateTime> nextBooking = null;
        if (item.getOwner().getId() == userId) {
            for (Booking booking : bookings) {
                if (booking.getEnd().isBefore(LocalDateTime.now())) {
                    lastBooking = List.of(booking.getStart(), booking.getEnd());
                } else {
                    nextBooking = List.of(booking.getStart(), booking.getEnd());
                }
            }
        }
        return ItemMapper.mapToItemResponseWithDatesAndCommentsDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemResponseWithDatesAndCommentsDto> findAllItemsByUserId(long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);

        return items.stream()
                .map(item -> {
                    List<CommentResponseDto> comments = commentRepository.findAllCommentByItem(item).stream()
                            .map(CommentMapper::mapToCommentResponseDto).toList();

                    Collection<Booking> bookings = bookingRepository.findLastAndNextBookingItem(item.getId());
                    List<LocalDateTime> lastBooking = null;
                    List<LocalDateTime> nextBooking = null;
                    if (item.getOwner().getId() == userId) {
                        for (Booking booking : bookings) {
                            if (booking.getEnd().isBefore(LocalDateTime.now())) {
                                lastBooking = List.of(booking.getStart(), booking.getEnd());
                            } else {
                                nextBooking = List.of(booking.getStart(), booking.getEnd());
                            }
                        }
                    }
                    return ItemMapper.mapToItemResponseWithDatesAndCommentsDto(item, lastBooking, nextBooking,
                            comments);
                }).toList();
    }

    @Override
    @Transactional
    public ItemResponseDto createItem(ItemCreateDto itemCreateDto) {
        User user = userRepository.findById(itemCreateDto.getOwner())
                .orElseThrow(() -> new NotFoundException(String.format("Нет пользователя с id = %d",
                        itemCreateDto.getOwner())));
        Item item = ItemMapper.mapToItem(itemCreateDto, user);
        item = itemRepository.save(item);
        return ItemMapper.mapToItemResponseDto(item);
    }

    @Override
    @Transactional
    public ItemResponseDto updateItem(ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findById(itemUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Нет вещи с id = %d", itemUpdateDto.getId())));

        if (!item.getOwner().getId().equals(itemUpdateDto.getOwner())) {
            throw new NotFoundException(String.format("У вещи с id = %d владелец не id = %d", itemUpdateDto.getId(),
                    itemUpdateDto.getOwner()));
        }
        if (itemUpdateDto.getName() != null) {
            item.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            item.setAvailable(itemUpdateDto.getAvailable());
        }
        if (itemUpdateDto.getRequest() != null) {
            item.setRequest(itemUpdateDto.getRequest());
        }
        return ItemMapper.mapToItemResponseDto(item);
    }

    @Override
    public List<ItemResponseDto> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        List<Item> items = itemRepository.findAllItemsByText(text);
        return items.stream()
                .map(ItemMapper::mapToItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentResponseDto createComment(ItemCreateCommentDto itemCreateCommentDto) {
        User user = userRepository.findById(itemCreateCommentDto.getUser())
                .orElseThrow(() -> new NotFoundException(String.format("Нет пользователя с id = %d",
                        itemCreateCommentDto.getUser())));

        Item item = itemRepository.findById(itemCreateCommentDto.getItem())
                .orElseThrow(() -> new NotFoundException(String.format("Нет вещи с id = %d",
                        itemCreateCommentDto.getItem())));

        if (!bookingRepository.existsByBookerAndItemAndStatusAndEndBefore(user, item, Status.APPROVED,
                LocalDateTime.now())) {
            throw new ValidationException(String.format("Нет такого бронирования (пользователь с id = %d, " +
                            "вещь с id = %d) или оно не завершено",
                    itemCreateCommentDto.getUser(), itemCreateCommentDto.getItem()));
        }

        Comment comment = commentRepository.save(CommentMapper.mapToComment(itemCreateCommentDto, item, user));
        return CommentMapper.mapToCommentResponseDto(comment);
    }
}