package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceImplTest {
    private final RequestServiceImpl requestService;
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private User requestor;
    private User user;
    private ItemRequest itemRequest;
    private ItemRequestCreateDto requestCreateDto;

    @BeforeEach
    void setUp() {
        requestor = new User();
        requestor.setName("Requestor");
        requestor.setEmail("requestor@ya.ru");
        requestor = userRepository.save(requestor);

        user = new User();
        user.setName("User");
        user.setEmail("user@ya.ru");
        user = userRepository.save(user);

        itemRequest = new ItemRequest();
        itemRequest.setDescription("Description");
        itemRequest.setRequestor(requestor);
        itemRequest = requestRepository.save(itemRequest);

        requestCreateDto = new ItemRequestCreateDto(requestor.getId(), "Description");
    }

    @Test
    void testCreateItemRequest() {
        ItemRequestResponseDto result = requestService.createItemRequest(requestCreateDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(requestCreateDto.getDescription(), result.getDescription());
        assertEquals(requestCreateDto.getRequestor(), result.getRequestor());
        assertNotNull(result.getCreated());

        Optional<ItemRequest> savedRequest = requestRepository.findById(result.getId());
        assertTrue(savedRequest.isPresent());
        assertEquals(requestCreateDto.getDescription(), savedRequest.get().getDescription());
    }

    @Test
    void testCreateItemRequestWithNonExistentUser() {
        ItemRequestCreateDto invalidDto = new ItemRequestCreateDto(50L, "Description");

        assertThrows(NotFoundException.class, () -> requestService.createItemRequest(invalidDto));
    }

    @Test
    void testFindListOfYourRequests() {
        Item item = new Item();
        item.setName("Monitor");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequestId(itemRequest.getId());
        itemRepository.save(item);

        List<ItemRequestResponseWithItemDto> result = requestService.findListOfYourRequests(requestor.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemRequest.getId(), result.get(0).getId());
        assertEquals(itemRequest.getDescription(), result.get(0).getDescription());
        assertNotNull(result.get(0).getItems());
        assertEquals(1, result.get(0).getItems().size());
        assertEquals("Monitor", result.get(0).getItems().get(0).getName());
    }


    @Test
    void testFindAll() {
        ItemRequest anotherRequest = new ItemRequest();
        anotherRequest.setDescription("Description");
        anotherRequest.setRequestor(user);
        requestRepository.save(anotherRequest);

        List<ItemRequestResponseDto> result = requestService.findAll(requestor.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Description", result.get(0).getDescription());
        assertEquals(user.getId(), result.get(0).getRequestor());
    }

    @Test
    void testFindByIdWithNonExistentRequest() {
        assertThrows(NotFoundException.class, () -> requestService.findById(50L));
    }

    @Test
    void testFindById() {
        Item item = new Item();
        item.setName("Laptop");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequestId(itemRequest.getId());
        itemRepository.save(item);

        ItemRequestResponseWithItemDto result = requestService.findById(itemRequest.getId());

        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(requestor.getId(), result.getRequestor());
        assertNotNull(result.getCreated());
        assertNotNull(result.getItems());
        assertEquals(1, result.getItems().size());
        assertEquals("Laptop", result.getItems().get(0).getName());
    }
}