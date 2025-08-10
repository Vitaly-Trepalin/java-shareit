package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemResponseDto findByIdItem(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Нет вещи с id = %d", itemId)));
        return itemMapper.mapToItemResponseDto(item);
    }

    @Override
    public Collection<ItemResponseDto> findAllItemsByUserId(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Нет пользователя с id = %d", userId)));
        Collection<Item> items = itemRepository.findAllByOwner(user);
        return items.stream()
                .map(itemMapper::mapToItemResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public ItemResponseDto createItem(ItemCreateDto itemCreateDto) {
        User user = userRepository.findById(itemCreateDto.getOwner())
                .orElseThrow(() -> new NotFoundException(String.format("Нет пользователя с id = %d",
                        itemCreateDto.getOwner())));
        Item item = itemMapper.mapToItem(itemCreateDto, user);
        item = itemRepository.save(item);
        return itemMapper.mapToItemResponseDto(item);
    }

    @Override
    @Transactional
    public ItemResponseDto updateItem(ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findById(itemUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Нет вещи с id = %d", itemUpdateDto.getId())));
        User user = userRepository.findById(itemUpdateDto.getOwner())
                .orElseThrow(() -> new NotFoundException(String.format("Нет пользователя с id = %d",
                        itemUpdateDto.getOwner())));
        if (!item.getOwner().equals(user)) {
            throw new NotFoundException(String.format("У вещи с id = %d владелец не id = %d", itemUpdateDto.getId(),
                    itemUpdateDto.getOwner()));
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
        if (itemUpdateDto.getRequest() != null) {
            item.setRequest(itemUpdateDto.getRequest());
        }
        return itemMapper.mapToItemResponseDto(item);
    }

    @Override
    public Collection<ItemResponseDto> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<Item> items = itemRepository.findAllItemsByText(text);
        return items.stream()
                .map(itemMapper::mapToItemResponseDto)
                .collect(Collectors.toList());
    }
}