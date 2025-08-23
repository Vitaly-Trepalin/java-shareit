package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseWithDatesAndCommentsDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemMapper {
    public static ItemResponseDto mapToItemResponseDto(Item item) {
        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setAvailable(item.isAvailable());
        itemResponseDto.setOwner(item.getOwner().getId());
        itemResponseDto.setRequestId(item.getRequestId());
        return itemResponseDto;
    }

    public static ItemResponseWithDatesAndCommentsDto mapToItemResponseWithDatesAndCommentsDto(
            Item item, List<LocalDateTime> lastBooking, List<LocalDateTime> nextBooking,
            List<CommentResponseDto> comments) {
        ItemResponseWithDatesAndCommentsDto itemResponseDto =
                new ItemResponseWithDatesAndCommentsDto();
        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setAvailable(item.isAvailable());
        itemResponseDto.setOwner(item.getOwner().getId());
        itemResponseDto.setRequestId(item.getRequestId());
        itemResponseDto.setLastBooking(lastBooking);
        itemResponseDto.setNextBooking(nextBooking);
        itemResponseDto.setComments(comments);
        return itemResponseDto;
    }

    public static Item mapToItem(ItemCreateDto itemDto, User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        item.setRequestId(itemDto.getRequestId());
        return item;
    }
}