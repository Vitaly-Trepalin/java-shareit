package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {

    public static ItemRequest mapToItemRequest(ItemRequestCreateDto request, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(request.getDescription());
        itemRequest.setRequestor(user);
        return itemRequest;
    }

    public static ItemRequestResponseDto mapToItemRequestResponseDto(ItemRequest itemRequest) {
        ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto();
        itemRequestResponseDto.setId(itemRequest.getId());
        itemRequestResponseDto.setDescription(itemRequest.getDescription());
        itemRequestResponseDto.setRequestor(itemRequest.getRequestor().getId());
        itemRequestResponseDto.setCreated(itemRequest.getCreated());
        return itemRequestResponseDto;
    }

    public static ItemRequestResponseWithItemDto mapToItemRequestResponseWithItemDto(ItemRequest itemRequest,
                                                                                     List<ItemResponseDto> items) {
        ItemRequestResponseWithItemDto itemRequestResponseDto = new ItemRequestResponseWithItemDto();
        itemRequestResponseDto.setId(itemRequest.getId());
        itemRequestResponseDto.setDescription(itemRequest.getDescription());
        itemRequestResponseDto.setRequestor(itemRequest.getRequestor().getId());
        itemRequestResponseDto.setCreated(itemRequest.getCreated());
        itemRequestResponseDto.setItems(items);
        return itemRequestResponseDto;
    }
}