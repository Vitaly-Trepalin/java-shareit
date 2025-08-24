package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.item.dto.ItemCreateCommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@RestController
@RequestMapping("/items")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(value = "X-Sharer-User-Id") @Positive long userId,
                                             @RequestBody @Valid ItemCreateDto itemCreateDto) {
        log.info("Method launched (createItem(long userId = {}, ItemCreateDto itemCreateDto = {}))",
                userId, itemCreateDto);
        return itemClient.createItem(userId, itemCreateDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItemsByUserId(@RequestHeader(value = "X-Sharer-User-Id")
                                                       @Positive long userId) {
        log.info("Method launched (findAllItemsByUserId(long userId = {}))", userId);
        return itemClient.findAllItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findByIdItem(@PathVariable @Positive long itemId,
                                               @RequestHeader(value = "X-Sharer-User-Id") @Positive long userId) {
        log.info("Method launched (findByIdItem(long itemId = {}))", itemId);
        return itemClient.findByIdItem(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(value = "X-Sharer-User-Id") @Positive long userId,
                                             @PathVariable @Positive long itemId,
                                             @RequestBody @Valid ItemUpdateDto itemUpdateDto) {
        log.info("Method launched (updateItem(long userId = {}, long itemId = {}, ItemDto itemDto = {}))", userId,
                itemId, itemUpdateDto);
        return itemClient.updateItem(itemId, userId, itemUpdateDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam() String text) {
        log.info("Method launched (searchItems(String text = {}))", text);
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(value = "X-Sharer-User-Id") @Positive long userId,
                                                @PathVariable @Positive long itemId,
                                                @RequestBody ItemCreateCommentDto itemCreateCommentDto) {
        log.info("Method launched (createComment(long userId = {}, long itemId = {}, " +
                "ItemCreateCommentDto itemCreateCommentDto{}))", userId, itemId, itemCreateCommentDto);
        return itemClient.createComment(itemId, userId, itemCreateCommentDto);
    }
}