package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> findByIdItem(@PathVariable long itemId) {
        return new ResponseEntity<>(itemService.findByIdItem(itemId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<ItemDto>> findAllItemsByUserId(@RequestHeader(value = "X-Sharer-User-Id")
                                                                    long userId) {
        return new ResponseEntity<>(itemService.findAllItemsByUserId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                              @RequestBody @Validated(Marker.OnCreate.class) ItemDto itemDto) {
        return new ResponseEntity<>(itemService.createItem(userId, itemDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                              @PathVariable long itemId,
                                              @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.updateItem(userId, itemId, itemDto), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemDto>> searchItems(@RequestParam() String text) {
        return new ResponseEntity<>(itemService.searchItems(text), HttpStatus.OK);
    }
}