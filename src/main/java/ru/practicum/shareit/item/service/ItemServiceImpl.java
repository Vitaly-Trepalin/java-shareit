package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repositories.ItemRepository;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public ItemDto findByIdItem(long itemId) {
        log.info("method launched (findByIdItem(long itemId))");
        return itemRepository.findByIdItem(itemId);
    }

    @Override
    public Collection<ItemDto> findAllItemsByUserId(long userId) {
        log.info("method launched (findAllItemsByUserId(long userId))");
        return itemRepository.findAllItemsByUserId(userId);
    }

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        log.info("method launched (createItem(long userId, ItemDto itemDto))");
        return itemRepository.createItem(userId, itemDto);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        log.info("method launched (updateItem(long userId, long itemId, ItemDto itemDto))");
        return itemRepository.updateItem(userId, itemId, itemDto);
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        log.info("method launched (searchItems(String text))");
        return itemRepository.searchItems(text);
    }
}