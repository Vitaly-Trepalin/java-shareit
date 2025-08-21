package ru.practicum.shareit.request;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

@RestController
@RequestMapping("/requests")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestService requestService;

    @PostMapping
    public ItemRequestResponseDto createItemRequest(@RequestHeader(value = "X-Sharer-User-Id") @Positive
                                                    long requestorId,
                                                    @RequestBody ItemRequestCreateDto request) {
        log.info("Method launched (createItemRequest(long userId = {}, ItemRequestCreateDto itemRequestCreateDto = {}))",
                requestorId, request);
        ItemRequestCreateDto newRequest = new ItemRequestCreateDto(requestorId, request.getDescription());
        return requestService.createItemRequest(newRequest);
    }

//    @GetMapping
//    public List<ItemRequestResponseDto> findByRequestor(@RequestHeader(value = "X-Sharer-User-Id") @Positive
//                                                       long userId) {
//        log.info("Method launched (findById(long userId = {}))", userId);
//        return requestService.findById(userId);
//    }
//
//    @GetMapping("/all")
//    public List<ItemRequestResponseDto> findAll(@RequestHeader(value = "X-Sharer-User-Id") @Positive long userId) {
//        log.info("Method launched (findAll(long userId = {}))", userId);
//        return requestService.findAll(userId);
//    }
//
//    @GetMapping("/{requestId}")
//    public List<ItemRequestResponseDto> findByRequestId(@RequestHeader(value = "X-Sharer-User-Id") @Positive
//                                                       long requestId) {
//        log.info("Method launched (findById(long userId = {}))", requestId);
//        return requestService.findByRequestId(requestId);
//    }
}