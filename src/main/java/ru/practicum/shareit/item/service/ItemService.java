package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;

public interface ItemService {
    ItemResponseDto findByIdItem(long itemId);

    Collection<ItemResponseDto> findAllItemsByUserId(long userId);

    ItemResponseDto createItem(ItemCreateDto itemCreateDto);

    ItemResponseDto updateItem(ItemUpdateDto itemUpdateDto);

    Collection<ItemResponseDto> searchItems(String text);
}