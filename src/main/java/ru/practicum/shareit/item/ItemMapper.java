package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.User;

@Component
public class ItemMapper {
    public ItemResponseDto mapToItemResponseDto(Item item) {
        return new ItemResponseDto(item.getId(), item.getName(), item.getDescription(), item.isAvailable(),
                item.getRequest());
//                item.getRequest() != null ? item.getRequest() : null);
    }

    public Item mapToItem(ItemCreateDto itemDto, User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        item.setRequest(item.getRequest());
        return item;
    }
}