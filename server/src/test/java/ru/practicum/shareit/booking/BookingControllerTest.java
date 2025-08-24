package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private BookingApproveDto bookingApproveDto;
    private BookingCreateDto bookingCreateDto;
    private BookingResponseDto bookingResponseDto;
    private User user1;
    private User user2;
    private Item item;

    @BeforeEach
    void setUp() {
        bookingApproveDto = new BookingApproveDto(1L, true);
        bookingCreateDto = new BookingCreateDto(LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 12, 31, 23, 59, 59),
                1L, 1L, Status.WAITING);

        user1 = new User();
        user1.setId(1L);
        user1.setName("Name1");
        user1.setEmail("email1@ya.ru");

        user2 = new User();
        user2.setId(2L);
        user2.setName("Name2");
        user2.setEmail("email2@ya.ru");

        item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user2);

        bookingResponseDto = new BookingResponseDto(1L, LocalDateTime.of(2024, 1, 1, 0, 0, 0),
                LocalDateTime.of(2024, 12, 31, 23, 59, 59),
                Status.WAITING, user1, item);
    }

    @Test
    void testCreateBooking() throws Exception {
        when(bookingService.createBooking(bookingCreateDto)).thenReturn(bookingResponseDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.start").value("2024-01-01T00:00:00"))
                .andExpect(jsonPath("$.end").value("2024-12-31T23:59:59"))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.booker.id").value(1L))
                .andExpect(jsonPath("$.item.id").value(1L));

        verify(bookingService, times(1)).createBooking(bookingCreateDto);
    }

    @Test
    void testApproveBooking() throws Exception {
        bookingResponseDto.setStatus(Status.APPROVED);

        when(bookingService.approveBooking(bookingApproveDto, 1L))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.start").value("2024-01-01T00:00:00"))
                .andExpect(jsonPath("$.end").value("2024-12-31T23:59:59"))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.booker.id").value(1L))
                .andExpect(jsonPath("$.item.id").value(1L));

        verify(bookingService, times(1)).approveBooking(bookingApproveDto, 1L);
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.start").value("2024-01-01T00:00:00"))
                .andExpect(jsonPath("$.end").value("2024-12-31T23:59:59"))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.booker.id").value(1L))
                .andExpect(jsonPath("$.item.id").value(1L));

        verify(bookingService, times(1)).getBooking(anyLong(), anyLong());
    }

    @Test
    void testGetAllBookingsByBooker() throws Exception {
        List<BookingResponseDto> bookings = List.of(bookingResponseDto);

        when(bookingService.getBookingsForUser(State.ALL, 1L))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].start").value("2024-01-01T00:00:00"))
                .andExpect(jsonPath("$[0].end").value("2024-12-31T23:59:59"))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(1L))
                .andExpect(jsonPath("$[0].item.id").value(1L));

        verify(bookingService, times(1)).getBookingsForUser(State.ALL, 1L);
    }

    @Test
    void testGetBookingsForUserItems() throws Exception {
        List<BookingResponseDto> bookings = List.of(bookingResponseDto);

        when(bookingService.getBookingsForUserItems(State.ALL, 1L))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].start").value("2024-01-01T00:00:00"))
                .andExpect(jsonPath("$[0].end").value("2024-12-31T23:59:59"))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(1L))
                .andExpect(jsonPath("$[0].item.id").value(1L));

        verify(bookingService, times(1)).getBookingsForUserItems(State.ALL, 1L);
    }
}
