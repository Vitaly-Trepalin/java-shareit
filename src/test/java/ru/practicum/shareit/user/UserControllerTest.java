//package ru.practicum.shareit.user;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.shareit.user.dto.UserRequestDto;
//import ru.practicum.shareit.user.service.UserService;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(UserController.class)
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private UserService userService;
//
//    private final long id1 = 1L;
//    private final String name1 = "Vitaly";
//    private final String email1 = "ya1@ya.ru";
//    private final UserRequestDto userDto1 = new UserRequestDto(id1, name1, email1);
//
//    private final long id2 = 2L;
//    private final String name2 = "Maksim";
//    private final String email2 = "ya2@ya.ru";
//    private final UserRequestDto userDto2 = new UserRequestDto(id2, name2, email2);
//
//    @Test
//    void testFindByIdUser() throws Exception {
//        when(userService.findByIdUser(1L)).thenReturn(userDto1);
//
//        mockMvc.perform(get("/users/{id}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id1))
//                .andExpect(jsonPath("$.name").value(name1))
//                .andExpect(jsonPath("$.email").value(email1));
//
//        verify(userService, times(1)).findByIdUser(1L);
//    }
//
//    @Test
//    void testFindAllUsers() throws Exception {
//        when(userService.findAllUsers()).thenReturn(List.of(userDto1, userDto2));
//
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id").value(id1))
//                .andExpect(jsonPath("$[0].name").value(name1))
//                .andExpect(jsonPath("$[0].email").value(email1))
//                .andExpect(jsonPath("$[1].id").value(id2))
//                .andExpect(jsonPath("$[1].name").value(name2))
//                .andExpect(jsonPath("$[1].email").value(email2));
//
//        verify(userService, times(1)).findAllUsers();
//    }
//
//    @Test
//    void testCreateUser() throws Exception {
//        UserRequestDto requestUser = new UserRequestDto(id1, name1, email1);
//        UserRequestDto expectedUser = new UserRequestDto(id1, name1, email1);
//
//        when(userService.createUser(requestUser)).thenReturn(expectedUser);
//
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(requestUser)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(id1))
//                .andExpect(jsonPath("$.name").value(name1))
//                .andExpect(jsonPath("$.email").value(email1));
//
//        verify(userService, times(1)).createUser(requestUser);
//    }
//
//    @Test
//    void testUpdateUser() throws Exception {
//        UserRequestDto requestUser = new UserRequestDto(null, name2, email2);
//        UserRequestDto expectedUser = new UserRequestDto(1L, name2, email2);
//
//        when(userService.updateUser(1L, requestUser)).thenReturn(expectedUser);
//
//        mockMvc.perform(patch("/users/1")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(requestUser)))
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.name").value(name2))
//                .andExpect(jsonPath("$.email").value(email2));
//
//        verify(userService, times(1)).updateUser(1L, requestUser);
//    }
//
//    @Test
//    void testDeleteUser() throws Exception {
//        doNothing().when(userService).deleteUser(1);
//
//        mockMvc.perform(delete("/users/{id}", 1L))
//                .andExpect(status().isOk());
//
//        verify(userService, times(1)).deleteUser(1L);
//    }
//}
