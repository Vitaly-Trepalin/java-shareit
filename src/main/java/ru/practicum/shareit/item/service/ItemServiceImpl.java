package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemResponseDto findByIdItem(long itemId) {
        Item item = itemRepository.findByIdItem(itemId);
        return itemMapper.toItemResponseDto(item);
    }

    @Override
    public Collection<ItemResponseDto> findAllItemsByUserId(long userId) {
        User owner = userRepository.findByIdUser(userId);
        Collection<Item> items = itemRepository.findAllItemsByUserId(owner);
        return items.stream()
                .map(itemMapper::toItemResponseDto)
                .toList();
    }

    @Override
    public ItemResponseDto createItem(ItemCreateDto itemCreateDto) {
        User owner = userRepository.findByIdUser(itemCreateDto.getOwner());
        Item item = itemMapper.toItem(itemCreateDto, owner);
        Item itemSaved = itemRepository.createItem(item);
        return itemMapper.toItemResponseDto(itemSaved);
    }

    @Override
    public ItemResponseDto updateItem(ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findByIdItem(itemUpdateDto.getId());
        User user = userRepository.findByIdUser(itemUpdateDto.getOwner());
        if (!item.getOwner().equals(user)) {
            throw new NotFoundException(String.format("У вещи с id = %d нет владельца с id = %d", item.getId(),
                    user.getId()));
        }
        if (itemUpdateDto.getName() != null) {
            item.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            item.setAvailable(itemUpdateDto.getAvailable());
        }
        itemRepository.updateItem(item);
        return itemMapper.toItemResponseDto(item);
    }

    @Override
    public Collection<ItemResponseDto> searchItems(String text) { // как то переделать поиск по именам и описаниям
        return itemRepository.searchItems(text);
    }
}