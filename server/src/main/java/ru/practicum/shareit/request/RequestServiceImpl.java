package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestResponseDto createItemRequest(ItemRequestCreateDto request) {
        User user = userRepository.findById(request.getRequestor())
                .orElseThrow(() -> new NotFoundException(String.format("Нет пользователя с id = %d",
                        request.getRequestor())));

        ItemRequest savedRequest = requestRepository.save(ItemRequestMapper.mapToItemRequest(request, user));
        return ItemRequestMapper.mapToItemRequestResponseDto(savedRequest);
    }

    @Override
    public List<ItemRequestResponseWithItemDto> findListOfYourRequests(long requestorId) {
        List<Long> requestIds = requestRepository.findRequestIdsByRequestorId(requestorId);

        if (requestIds == null) {
            throw new NotFoundException(String.format("У пользователя user = %d нет своих запросов", requestorId));
        }

        Map<Long, List<ItemResponseDto>> itemsByRequestId = itemRepository.findAllItemByRequestIdIn(requestIds).stream()
                .collect(Collectors.groupingBy(Item::getRequestId,
                        Collectors.mapping(ItemMapper::mapToItemResponseDto, Collectors.toList())));


        List<ItemRequest> requests = requestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId);

        return requests.stream()
                .map(request -> ItemRequestMapper.mapToItemRequestResponseWithItemDto(request,
                        itemsByRequestId.getOrDefault(request.getId(), Collections.emptyList())))
                .toList();
    }

    @Override
    public List<ItemRequestResponseDto> findAll(long requestor) {
        List<ItemRequest> requests = requestRepository.findByRequestorIdNotOrderByCreatedDesc(requestor);
        return requests.stream()
                .map(ItemRequestMapper::mapToItemRequestResponseDto)
                .toList();
    }

    @Override
    public ItemRequestResponseWithItemDto findById(long requestId) {
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Нет запроса с id = %d",
                        requestId)));

        return ItemRequestMapper.mapToItemRequestResponseWithItemDto(itemRequest,
                itemRepository.findAllItemByRequestId(itemRequest.getId()).stream()
                        .map(ItemMapper::mapToItemResponseDto)
                        .toList());
    }
}