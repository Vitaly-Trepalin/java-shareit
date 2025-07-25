package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto findByIdItem(long itemId);

    Collection<ItemDto> findAllItemsByUserId(long userId);

    ItemDto createItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userid, long itemId, ItemDto itemDto);

    Collection<ItemDto> searchItems(String text);
}