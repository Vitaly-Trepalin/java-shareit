package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateCommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseWithDatesAndCommentsDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemResponseDto itemResponseDto;
    private ItemResponseWithDatesAndCommentsDto itemResponseWithDatesAndCommentsDto;
    private ItemCreateDto itemCreateDto;
    private ItemUpdateDto itemUpdateDto;
    private CommentResponseDto commentResponseDto;
    private ItemCreateCommentDto itemCreateCommentDto;

    @BeforeEach
    void setUp() {
        itemResponseDto = new ItemResponseDto(1L, "Item", "Description", true,
                1L, null);
        itemResponseWithDatesAndCommentsDto = new ItemResponseWithDatesAndCommentsDto(1L, "Item",
                "Description", true, 1L, null,
                null, null, Collections.emptyList());
        itemCreateDto = new ItemCreateDto("Item", "Description", true,
                1L, null);
        itemUpdateDto = new ItemUpdateDto(1L, "Item", "Description",
                false, 1L, null);
        commentResponseDto = new CommentResponseDto(1L, "Comment", "Name",
                LocalDateTime.now());
        itemCreateCommentDto = new ItemCreateCommentDto(1L, 1L, "Great item!");
    }

    @Test
    void testFindByIdItem() throws Exception {
        when(itemService.findByIdItem(anyLong(), anyLong())).thenReturn(itemResponseWithDatesAndCommentsDto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Item"))
                .andExpect(jsonPath("$.description").value("Description"));

        verify(itemService, times(1)).findByIdItem(anyLong(), anyLong());
    }

    @Test
    void testFindAllItemsByUserId() throws Exception {
        when(itemService.findAllItemsByUserId(anyLong())).thenReturn(List.of(itemResponseWithDatesAndCommentsDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Item"))
                .andExpect(jsonPath("$[0].description").value("Description"));

        verify(itemService, times(1)).findAllItemsByUserId(anyLong());
    }

    @Test
    void testCreateItem() throws Exception {
        when(itemService.createItem(any(ItemCreateDto.class))).thenReturn(itemResponseDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Item"))
                .andExpect(jsonPath("$.description").value("Description"));

        verify(itemService, times(1)).createItem(any(ItemCreateDto.class));
    }

    @Test
    void testUpdateItem() throws Exception {
        when(itemService.updateItem(any(ItemUpdateDto.class))).thenReturn(itemResponseDto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Item"))
                .andExpect(jsonPath("$.description").value("Description"));

        verify(itemService, times(1)).updateItem(any(ItemUpdateDto.class));
    }

    @Test
    void testSearchItems() throws Exception {
        when(itemService.searchItems(anyString())).thenReturn(List.of(itemResponseDto));

        mockMvc.perform(get("/items/search")
                        .param("text", "Desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Item"))
                .andExpect(jsonPath("$[0].description").value("Description"));

        verify(itemService, times(1)).searchItems(anyString());
    }

    @Test
    void testSearchItemsShouldReturnEmptyList() throws Exception {
        when(itemService.searchItems(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param("text", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(itemService, times(1)).searchItems(anyString());
    }

    @Test
    void testCreateComment() throws Exception {
        when(itemService.createComment(any(ItemCreateCommentDto.class))).thenReturn(commentResponseDto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemCreateCommentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("Comment"))
                .andExpect(jsonPath("$.authorName").value("Name"));

        verify(itemService, times(1)).createComment(any(ItemCreateCommentDto.class));
    }
}