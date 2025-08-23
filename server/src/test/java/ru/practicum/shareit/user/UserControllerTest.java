package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final long id1 = 1L;
    private final String name1 = "Vitaly";
    private final String email1 = "ya1@ya.ru";
    private final UserResponseDto userDto1 = new UserResponseDto(id1, name1, email1);

    private final long id2 = 2L;
    private final String name2 = "Maksim";
    private final String email2 = "ya2@ya.ru";
    private final UserResponseDto userDto2 = new UserResponseDto(id2, name2, email2);

    @Test
    void testFindByIdUser() throws Exception {
        when(userService.findByIdUser(1L)).thenReturn(userDto1);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.email").value(email1));

        verify(userService, times(1)).findByIdUser(1L);
    }

    @Test
    void testFindAllUsers() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of(userDto1, userDto2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].name").value(name1))
                .andExpect(jsonPath("$[0].email").value(email1))
                .andExpect(jsonPath("$[1].id").value(id2))
                .andExpect(jsonPath("$[1].name").value(name2))
                .andExpect(jsonPath("$[1].email").value(email2));

        verify(userService, times(1)).findAllUsers();
    }

    @Test
    void testCreateUser() throws Exception {
        UserCreateDto requestUser = new UserCreateDto(name1, email1);
        UserResponseDto expectedUser = new UserResponseDto(id1, name1, email1);

        when(userService.createUser(requestUser)).thenReturn(expectedUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.email").value(email1));

        verify(userService, times(1)).createUser(requestUser);
    }

    @Test
    void testUpdateUser() throws Exception {
        UserUpdateDto requestUser = new UserUpdateDto(1L, name1, email1);
        UserResponseDto expectedUser = new UserResponseDto(1L, name1, email1);

        when(userService.updateUser(requestUser)).thenReturn(expectedUser);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(requestUser)))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.email").value(email1));

        verify(userService, times(1)).updateUser(requestUser);
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1L);
    }
}