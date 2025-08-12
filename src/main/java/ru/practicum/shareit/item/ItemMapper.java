package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseWithCommentsDto;
import ru.practicum.shareit.item.dto.ItemResponseWithDatesAndCommentsDto;
import ru.practicum.shareit.user.User;

import java.util.List;

public class ItemMapper {
    public static ItemResponseDto mapToItemResponseDto(Item item) {
        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setAvailable(item.isAvailable());
        itemResponseDto.setOwner(item.getOwner().getId());
        itemResponseDto.setRequest(item.getRequest());
        return itemResponseDto;
    }

    public static ItemResponseWithCommentsDto mapToItemResponseAndCommentsDto(Item item, List<CommentResponseDto> comments) {
        ItemResponseWithCommentsDto itemResponseDto = new ItemResponseWithCommentsDto();
        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setAvailable(item.isAvailable());
        itemResponseDto.setOwner(item.getOwner().getId());
        itemResponseDto.setRequest(item.getRequest());
        itemResponseDto.setComments(comments);
        return itemResponseDto;
    }

    public static ItemResponseWithDatesAndCommentsDto mapToItemResponseWithDatesAndCommentsDto(Item item, List<CommentResponseDto> comments) {
        ItemResponseWithDatesAndCommentsDto itemBookingResponseDto = new ItemResponseWithDatesAndCommentsDto();
        itemBookingResponseDto.setId(item.getId());
        itemBookingResponseDto.setName(item.getName());
        itemBookingResponseDto.setDescription(item.getDescription());
        itemBookingResponseDto.setAvailable(item.isAvailable());
        itemBookingResponseDto.setOwner(item.getOwner().getId());
        itemBookingResponseDto.setRequest(item.getRequest());
        itemBookingResponseDto.setComments(comments);
        return itemBookingResponseDto;
    }

    public static Item mapToItem(ItemCreateDto itemDto, User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        item.setRequest(item.getRequest());
        return item;
    }
}