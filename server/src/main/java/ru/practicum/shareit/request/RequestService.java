package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemDto;

import java.util.List;

public interface RequestService {
    ItemRequestResponseDto createItemRequest(ItemRequestCreateDto request);

    List<ItemRequestResponseWithItemDto> findListOfYourRequests(long requestor);

    List<ItemRequestResponseDto> findAll(long requestor);

    ItemRequestResponseWithItemDto findById(long requestId);
}