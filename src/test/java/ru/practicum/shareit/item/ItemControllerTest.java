package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
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

    private final long id1 = 1L;
    private final String name1 = "item1";
    private final String description1 = "description1";
    private final boolean available1 = true;
    private final ItemDto itemDto1 = new ItemDto(id1, name1, description1, available1, null);

    private final long id2 = 2L;
    private final String name2 = "item2";
    private final String description2 = "description2";
    private final boolean available2 = true;
    private final ItemDto itemDto2 = new ItemDto(id2, name2, description2, available2, null);

    @Test
    void testFindByIdItem() throws Exception {
        when(itemService.findByIdItem(1L)).thenReturn(itemDto1);

        mockMvc.perform(get("/items/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.description").value(description1))
                .andExpect(jsonPath("$.available").value(available1));

        verify(itemService, times(1)).findByIdItem(1L);
    }

    @Test
    void testFindAllItemsByUserId() throws Exception {
        List<ItemDto> items = List.of(itemDto1, itemDto2);

        when(itemService.findAllItemsByUserId(1L)).thenReturn(items);

        mockMvc.perform(get("/items").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].name").value(name1))
                .andExpect(jsonPath("$[1].id").value(id2))
                .andExpect(jsonPath("$[1].name").value(name2));
    }

    @Test
    void testCreateItem() throws Exception {
        ItemDto expectedItem = new ItemDto(id1, name1, description1, available1, null);

        when(itemService.createItem(1L, itemDto1)).thenReturn(expectedItem);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(itemDto1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.description").value(description1))
                .andExpect(jsonPath("$.available").value(available1));

        verify(itemService, times(1)).createItem(1L, itemDto1);
    }

    @Test
    void testUpdateItem() throws Exception {
        ItemDto requestItem = new ItemDto(id2, name2, description2, available2, null);
        ItemDto expectedItem = new ItemDto(id1, name2, description2, available2, null);

        when(itemService.updateItem(1L, 1L, requestItem)).thenReturn(expectedItem);

        mockMvc.perform(patch("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name2))
                .andExpect(jsonPath("$.description").value(description2))
                .andExpect(jsonPath("$.available").value(available2));

        verify(itemService, times(1)).updateItem(1L, 1L, requestItem);
    }

    @Test
    void testSearchItems() throws Exception {
        when(itemService.searchItems(anyString())).thenReturn(List.of(itemDto1));

        mockMvc.perform(get("/items/search")
                        .param("text", "description1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].name").value(name1))
                .andExpect(jsonPath("$[0].description").value(description1))
                .andExpect(jsonPath("$[0].available").value(available1));

        verify(itemService, times(1)).searchItems("description1");
    }
}