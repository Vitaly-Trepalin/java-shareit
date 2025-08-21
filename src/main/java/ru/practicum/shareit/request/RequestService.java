package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

public interface RequestService {
    ItemRequestResponseDto createItemRequest(ItemRequestCreateDto request);
}