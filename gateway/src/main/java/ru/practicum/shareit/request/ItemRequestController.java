package ru.practicum.shareit.request;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@RestController
@RequestMapping("/requests")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(value = "X-Sharer-User-Id") @Positive
                                                    long requestor,
                                                    @RequestBody ItemRequestCreateDto request) {
        log.info("Method launched (createItemRequest(long userId = {}, " +
                        "ItemRequestCreateDto itemRequestCreateDto = {}))",
                requestor, request);
        return itemRequestClient.createItemRequest(requestor, request);
    }

    @GetMapping
    public ResponseEntity<Object> findListOfYourRequests(@RequestHeader(value = "X-Sharer-User-Id")
                                                         @Positive long requestor) {
        log.info("Method launched (findListOfYourRequests(long requestorId = {}))", requestor);
        return itemRequestClient.findListOfYourRequests(requestor);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestHeader(value = "X-Sharer-User-Id") @Positive long requestor) {
        log.info("Method launched (findAll(long userId = {}))", requestor);
        return itemRequestClient.findAll(requestor);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@PathVariable long requestId) {
        log.info("Method launched (findById(long requestId = {}))", requestId);
        return itemRequestClient.findById(requestId);
    }
}