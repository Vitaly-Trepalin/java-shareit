package ru.practicum.shareit.item.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;

    @Override
    public Item findByIdItem(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Нет вещи с id = " + itemId);
        }
        return items.get(itemId);
    }

    @Override
    public Collection<Item> findAllItemsByUserId(User owner) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(owner))
                .toList();
    }

    @Override
    public Item createItem(Item item) {
        Item newItem = new Item(idGenerator.getAndIncrement(), item.getName(), item.getDescription(),
                item.isAvailable(), item.getOwner(), item.getRequest());
        items.put(newItem.getId(), newItem);
        return newItem;
    }

    @Override
    public void updateItem(Item item) {
        items.put(item.getId(), item);
    }

    @Override
    public Collection<ItemResponseDto> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        List<Item> matchingNames = items.values().stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()))
                .toList();
        List<Item> matchingDescriptions = items.values().stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .toList();
        return Stream.concat(matchingNames.stream(), matchingDescriptions.stream())
                .distinct()
                .map(itemMapper::toItemResponseDto)
                .toList();
    }
}