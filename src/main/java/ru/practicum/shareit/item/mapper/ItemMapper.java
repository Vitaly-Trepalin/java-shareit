package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest() != null ? item.getRequest() : null);
    }

    public Item toItem(Long userId, AtomicLong idGenerator, ItemDto itemDto) {
        return new Item(idGenerator.getAndIncrement(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userId,
                itemDto.getRequest());
    }
}