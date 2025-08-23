package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemDto;

import java.util.List;

@RestController
@RequestMapping("/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestService requestService;

    @PostMapping
    public ItemRequestResponseDto createItemRequest(@RequestHeader(value = "X-Sharer-User-Id") long requestor,
                                                    @RequestBody ItemRequestCreateDto request) {
        log.info("Method launched (createItemRequest(long userId = {}, " +
                        "ItemRequestCreateDto itemRequestCreateDto = {}))",
                requestor, request);
        ItemRequestCreateDto newRequest = new ItemRequestCreateDto(requestor, request.getDescription());
        return requestService.createItemRequest(newRequest);
    }

    @GetMapping
    public List<ItemRequestResponseWithItemDto> findListOfYourRequests(@RequestHeader(value = "X-Sharer-User-Id")
                                                                       long requestor) {
        log.info("Method launched (findListOfYourRequests(long requestorId = {}))", requestor);
        return requestService.findListOfYourRequests(requestor);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> findAll(@RequestHeader(value = "X-Sharer-User-Id") long requestor) {
        log.info("Method launched (findAll(long userId = {}))", requestor);
        return requestService.findAll(requestor);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseWithItemDto findById(@PathVariable long requestId) {
        log.info("Method launched (findById(long requestId = {}))", requestId);
        return requestService.findById(requestId);
    }
}