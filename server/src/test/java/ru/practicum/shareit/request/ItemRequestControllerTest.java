package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestService requestService;

    private ItemRequestCreateDto itemRequestCreateDto;
    private ItemRequestResponseDto itemRequestResponseDto;
    private ItemRequestResponseWithItemDto itemRequestResponseWithItemDto;
    private ItemResponseDto itemResponseDto;

    @BeforeEach
    void setUp() {
        itemRequestCreateDto = new ItemRequestCreateDto(1L, "Description");
        itemRequestResponseDto = new ItemRequestResponseDto(1L, "Description", 1L,
                LocalDateTime.of(2024, 12, 31, 23, 59, 59));

        itemResponseDto = new ItemResponseDto(1L, "Item", "Description", true,
                1L, 1L);
        itemRequestResponseWithItemDto = new ItemRequestResponseWithItemDto(1L, "Description", 2L,
                LocalDateTime.of(2024, 12, 31, 23, 59, 59),
                List.of(itemResponseDto));
    }

    @Test
    void testCreateItemRequest() throws Exception {
        when(requestService.createItemRequest(itemRequestCreateDto)).thenReturn(itemRequestResponseDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.created").value("2024-12-31 23:59:59"));

        verify(requestService, times(1)).createItemRequest(itemRequestCreateDto);
    }

    @Test
    void testFindListOfYourRequests() throws Exception {
        when(requestService.findListOfYourRequests(1L)).thenReturn(List.of(itemRequestResponseWithItemDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Description"))
                .andExpect(jsonPath("$[0].requestor").value(2L))
                .andExpect(jsonPath("$[0].created").value("2024-12-31 23:59:59"))
                .andExpect(jsonPath("$[0].items[0].id").value(1L));

        verify(requestService, times(1)).findListOfYourRequests(1L);
    }

    @Test
    void testFindAll() throws Exception {
        itemRequestResponseDto.setRequestor(2L);

        when(requestService.findAll(1L)).thenReturn(List.of(itemRequestResponseDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Description"))
                .andExpect(jsonPath("$[0].requestor").value(2L))
                .andExpect(jsonPath("$[0].created").value("2024-12-31 23:59:59"));

        verify(requestService, times(1)).findAll(1L);
    }

    @Test
    void testFindById() throws Exception {
        when(requestService.findById(1L)).thenReturn(itemRequestResponseWithItemDto);

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.requestor").value(2L))
                .andExpect(jsonPath("$.created").value("2024-12-31 23:59:59"));

        verify(requestService, times(1)).findById(1L);
    }
}