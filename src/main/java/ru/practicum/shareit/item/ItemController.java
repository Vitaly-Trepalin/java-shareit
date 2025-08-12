package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateCommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseWithDatesAndCommentsDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseWithDatesAndCommentsDto> findByIdItem(@PathVariable @Positive long itemId,
                                                                            @RequestHeader(value = "X-Sharer-User-Id")
                                                                            @Positive long userId) {
        log.info("Method launched (findByIdItem(long itemId = {}))", itemId);
        return new ResponseEntity<>(itemService.findByIdItem(itemId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<ItemResponseWithDatesAndCommentsDto>> findAllItemsByUserId(@RequestHeader(value = "X-Sharer-User-Id")
                                                                                                @Positive long userId) {
        log.info("Method launched (findAllItemsByUserId(long userId = {}))", userId);
        return new ResponseEntity<>(itemService.findAllItemsByUserId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@RequestHeader(value = "X-Sharer-User-Id") @Positive long userId,
                                                      @RequestBody @Valid ItemCreateDto itemCreateDto) {
        log.info("Method launched (createItem(long userId = {}, ItemCreateDto itemCreateDto = {}))",
                userId, itemCreateDto);
        ItemCreateDto newItemCreateDto = new ItemCreateDto(itemCreateDto.getName(), itemCreateDto.getDescription(),
                itemCreateDto.getAvailable(), userId, itemCreateDto.getRequest());
        return new ResponseEntity<>(itemService.createItem(newItemCreateDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> updateItem(@RequestHeader(value = "X-Sharer-User-Id") @Positive long userId,
                                                      @PathVariable @Positive long itemId,
                                                      @RequestBody @Valid ItemUpdateDto itemUpdateDto) {
        log.info("Method launched (updateItem(long userId = {}, long itemId = {}, ItemDto itemDto = {}))", userId,
                itemId, itemUpdateDto);
        ItemUpdateDto newItemUpdateDto = new ItemUpdateDto(itemId, itemUpdateDto.getName(),
                itemUpdateDto.getDescription(), itemUpdateDto.getAvailable(), userId,
                itemUpdateDto.getRequest());
        return new ResponseEntity<>(itemService.updateItem(newItemUpdateDto), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemResponseDto>> searchItems(@RequestParam() String text) {
        log.info("Method launched (searchItems(String text = {}))", text);
        return new ResponseEntity<>(itemService.searchItems(text), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentResponseDto> createComment(@RequestHeader(value = "X-Sharer-User-Id") @Positive
                                                            long userId,
                                                            @PathVariable @Positive long itemId,
                                                            @RequestBody ItemCreateCommentDto itemCreateCommentDto) {
        log.info("Method launched (createComment(long userId = {}, long itemId = {}, " +
                "ItemCreateCommentDto itemCreateCommentDto{}))", userId, itemId, itemCreateCommentDto);
        ItemCreateCommentDto newItemCreateCommentDto = new ItemCreateCommentDto(itemId, userId,
                itemCreateCommentDto.getText());
        return new ResponseEntity<>(itemService.createComment(newItemCreateCommentDto), HttpStatus.CREATED);
    }
}