package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;
    private UserResponseDto userResponseDto1;
    private UserResponseDto userResponseDto2;

    @BeforeEach
    void setUp() {
        userCreateDto = new UserCreateDto("Name1", "email1@ya.ru");
        userUpdateDto = new UserUpdateDto(1L, "Name1", "email1@ya.ru");
        userResponseDto1 = new UserResponseDto(1L, "Name1", "email1@ya.ru");
        userResponseDto2 = new UserResponseDto(2L, "Name2", "email2@ya.ru");
    }

    @Test
    void testFindByIdUser() throws Exception {
        when(userService.findByIdUser(anyLong())).thenReturn(userResponseDto1);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Name1"))
                .andExpect(jsonPath("$.email").value("email1@ya.ru"));

        verify(userService, times(1)).findByIdUser(anyLong());
    }

    @Test
    void testFindAllUsers() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of(userResponseDto1, userResponseDto2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Name1"))
                .andExpect(jsonPath("$[0].email").value("email1@ya.ru"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Name2"))
                .andExpect(jsonPath("$[1].email").value("email2@ya.ru"));

        verify(userService, times(1)).findAllUsers();
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.createUser(userCreateDto)).thenReturn(userResponseDto1);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Name1"))
                .andExpect(jsonPath("$.email").value("email1@ya.ru"));

        verify(userService, times(1)).createUser(userCreateDto);
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(userUpdateDto)).thenReturn(userResponseDto1);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(userUpdateDto)))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Name1"))
                .andExpect(jsonPath("$.email").value("email1@ya.ru"));

        verify(userService, times(1)).updateUser(userUpdateDto);
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1L);
    }
}