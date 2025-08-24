package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateCommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseWithDatesAndCommentsDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    private final ItemServiceImpl itemService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    private User owner;
    private User booker;
    private Item item;
    private ItemCreateDto itemCreateDto;
    private ItemUpdateDto itemUpdateDto;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@ya.ru");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@ya.ru");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        itemCreateDto = new ItemCreateDto("newItem", "newDescription", true, owner.getId(),
                null);
        itemUpdateDto = new ItemUpdateDto(item.getId(), "updateItem", "updateDescription",
                false, owner.getId(), null);
    }

    @Test
    void testCreateItem() {
        ItemResponseDto result = itemService.createItem(itemCreateDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(itemCreateDto.getName(), result.getName());
        assertEquals(itemCreateDto.getDescription(), result.getDescription());
        assertEquals(itemCreateDto.getAvailable(), result.getAvailable());
        assertEquals(itemCreateDto.getOwner(), result.getOwner());

        Optional<Item> savedItem = itemRepository.findById(result.getId());
        assertTrue(savedItem.isPresent());
        assertEquals(itemCreateDto.getName(), savedItem.get().getName());
    }

    @Test
    void testCreateItemWithNonExistentOwner() {
        ItemCreateDto invalidDto = new ItemCreateDto("Item", "Desc", true, 50L, null);

        assertThrows(NotFoundException.class, () -> itemService.createItem(invalidDto));
    }

    @Test
    void testFindByIdItem() {
        ItemResponseWithDatesAndCommentsDto result = itemService.findByIdItem(item.getId(), owner.getId());

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertNotNull(result.getComments());
    }

    @Test
    void testFindByIdItemWithNonExistentItem() {
        assertThrows(NotFoundException.class, () -> itemService.findByIdItem(50L, owner.getId()));
    }

    @Test
    void testFindAllItemsByUserId() {
        Item anotherItem = new Item();
        anotherItem.setName("2Item");
        anotherItem.setDescription("2Description");
        anotherItem.setAvailable(true);
        anotherItem.setOwner(owner);
        itemRepository.save(anotherItem);

        List<ItemResponseWithDatesAndCommentsDto> result = itemService.findAllItemsByUserId(owner.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(i -> i.getName().equals("Item")));
        assertTrue(result.stream().anyMatch(i -> i.getName().equals("2Item")));
    }

    @Test
    void testUpdateItem() {
        ItemResponseDto result = itemService.updateItem(itemUpdateDto);

        assertNotNull(result);
        assertEquals(itemUpdateDto.getId(), result.getId());
        assertEquals(itemUpdateDto.getName(), result.getName());
        assertEquals(itemUpdateDto.getDescription(), result.getDescription());
        assertEquals(itemUpdateDto.getAvailable(), result.getAvailable());

        Optional<Item> updatedItem = itemRepository.findById(item.getId());
        assertTrue(updatedItem.isPresent());
        assertEquals("updateItem", updatedItem.get().getName());
        assertEquals("updateDescription", updatedItem.get().getDescription());
        assertFalse(updatedItem.get().isAvailable());
    }

    @Test
    void testUpdateItemWithNonExistentItem() {
        ItemUpdateDto invalidDto = new ItemUpdateDto(999L, "Name", "Desc", true,
                owner.getId(), null);

        assertThrows(NotFoundException.class, () -> itemService.updateItem(invalidDto));
    }

    @Test
    void testUpdateItemByNonOwner() {
        ItemUpdateDto invalidDto = new ItemUpdateDto(item.getId(), "Name", "Desc", true,
                booker.getId(), null);

        assertThrows(NotFoundException.class, () -> itemService.updateItem(invalidDto));
    }

    @Test
    void testSearchItems() {
        Item item1 = new Item();
        item1.setName("laptop Dell");
        item1.setDescription("Description");
        item1.setAvailable(true);
        item1.setOwner(owner);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Dell monitor");
        item2.setDescription("Description");
        item2.setAvailable(true);
        item2.setOwner(owner);
        itemRepository.save(item2);

        Item item3 = new Item();
        item3.setName("keyboard");
        item3.setDescription("Description");
        item3.setAvailable(false);
        item3.setOwner(owner);
        itemRepository.save(item3);

        List<ItemResponseDto> result = itemService.searchItems("Dell");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(i -> i.getName().contains("Dell")));
    }

    @Test
    void testCreateComment() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.APPROVED);
        bookingRepository.save(booking);

        ItemCreateCommentDto commentDto = new ItemCreateCommentDto(item.getId(), booker.getId(), "Normik");

        CommentResponseDto result = itemService.createComment(commentDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Normik", result.getText());
        assertEquals("Booker", result.getAuthorName());
        assertNotNull(result.getCreated());

        List<Comment> comments = commentRepository.findAll();
        assertEquals(1, comments.size());
        assertEquals("Normik", comments.get(0).getText());
    }

    @Test
    void testCreateCommentWithoutBooking() {
        ItemCreateCommentDto commentDto = new ItemCreateCommentDto(item.getId(), booker.getId(), "Comment");

        assertThrows(ValidationException.class, () -> itemService.createComment(commentDto));
    }

    @Test
    void testCreateCommentForNonExistentItem() {
        ItemCreateCommentDto commentDto = new ItemCreateCommentDto(50L, booker.getId(), "Comment");

        assertThrows(NotFoundException.class, () -> itemService.createComment(commentDto));
    }

    @Test
    void testCreateCommentByNonExistentUser() {
        ItemCreateCommentDto commentDto = new ItemCreateCommentDto(item.getId(), 50L, "Comment");

        assertThrows(NotFoundException.class, () -> itemService.createComment(commentDto));
    }
}