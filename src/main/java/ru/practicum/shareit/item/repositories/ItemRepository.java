package ru.practicum.shareit.item.repositories;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemRepository {
    ItemDto findByIdItem(long itemId);

    Collection<ItemDto> findAllItemsByUserId(long userId);

    ItemDto createItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    Collection<ItemDto> searchItems(String text);
}