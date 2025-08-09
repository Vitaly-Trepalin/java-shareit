package ru.practicum.shareit.item.repositories;


import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface OldItemRepository {
    Item findByIdItem(long itemId);

    Collection<Item> findAllItemsByUserId(User user);

    Item createItem(Item item);

    void updateItem(Item item);

    Collection<ItemResponseDto> searchItems(String text);
}