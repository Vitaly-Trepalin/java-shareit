package ru.practicum.shareit.item.repositories;


import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface ItemRepository {
    Item findByIdItem(long itemId);

    Collection<Item> findAllItemsByUserId(User user);

    Item createItem(Item item);

    void updateItem(Item item);

    Collection<ItemResponseDto> searchItems(String text);
}