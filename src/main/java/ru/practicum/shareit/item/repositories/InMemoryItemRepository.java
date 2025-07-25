package ru.practicum.shareit.item.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
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
    public ItemDto findByIdItem(long itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new NotFoundException("Нет вещи с id = " + itemId);
        }
        return itemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> findAllItemsByUserId(long userId) {
        checkingForUserPresence(userId);
        return items.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .map(itemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        checkingForUserPresence(userId);
        Item item = itemMapper.toItem(userId, idGenerator, itemDto);
        items.put(item.getId(), item);
        return itemMapper.toItemDto(items.get(idGenerator.get() - 1));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        if (userId != items.get(itemId).getOwner()) {
            throw new NotFoundException(String.format("У вещи с id = %d нет владельца с id = %d", itemId, userId));
        }
        Item item = items.get(itemId);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return itemMapper.toItemDto(items.get(itemId));
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
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
                .map(itemMapper::toItemDto)
                .toList();
    }

    private void checkingForUserPresence(long userId) {
        userRepository.findByIdUser(userId);
    }
}