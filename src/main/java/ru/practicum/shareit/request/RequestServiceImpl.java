package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestResponseDto createItemRequest(ItemRequestCreateDto request) {
        User user = userRepository.findById(request.getRequestorId())
                .orElseThrow(() -> new NotFoundException(String.format("Нет пользователя с id = %d",
                        request.getRequestorId())));

        ItemRequest savedRequest = requestRepository.save(ItemRequestMapper.mapToItemRequest(request, user));
        return ItemRequestMapper.mapToItemRequestResponseDto(savedRequest);
    }
}
