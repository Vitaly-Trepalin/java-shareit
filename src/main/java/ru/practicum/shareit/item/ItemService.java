package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateCommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseWithDatesAndCommentsDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;

public interface ItemService {
    ItemResponseWithDatesAndCommentsDto findByIdItem(long itemId, long userId);

    Collection<ItemResponseWithDatesAndCommentsDto> findAllItemsByUserId(long userId);

    ItemResponseDto createItem(ItemCreateDto itemCreateDto);

    ItemResponseDto updateItem(ItemUpdateDto itemUpdateDto);

    Collection<ItemResponseDto> searchItems(String text);

    CommentResponseDto createComment(ItemCreateCommentDto itemCreateCommentDto);
}