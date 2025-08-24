package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    private UserCreateDto userCreateDto;
    private User user;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userCreateDto = new UserCreateDto("Name1", "email1@ya.ru");

        user = new User();
        user.setName("Name1");
        user.setEmail("email1@ya.ru");

        user1 = new User();
        user1.setName("User1");
        user1.setEmail("user1@ya.ru");

        user2 = new User();
        user2.setName("User2");
        user2.setEmail("user2@ya.ru");
    }

    @Test
    void testCreateUser() {
        UserResponseDto result = userService.createUser(userCreateDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(userCreateDto.getName(), result.getName());
        assertEquals(userCreateDto.getEmail(), result.getEmail());

        Optional<User> savedUser = userRepository.findById(result.getId());
        assertTrue(savedUser.isPresent());
        assertEquals(userCreateDto.getName(), savedUser.get().getName());
        assertEquals(userCreateDto.getEmail(), savedUser.get().getEmail());
    }

    @Test
    void testFindByIdUser() {
        User savedUser = userRepository.save(user);

        UserResponseDto result = userService.findByIdUser(savedUser.getId());

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getName(), result.getName());
        assertEquals(savedUser.getEmail(), result.getEmail());
    }

    @Test
    void testFindByIdUserWhenUserNotExists() {
        long nonExistentUserId = 10L;

        assertThrows(NotFoundException.class, () -> userService.findByIdUser(nonExistentUserId));
    }

    @Test
    void testFindAllUsers() {
        userRepository.save(user1);
        userRepository.save(user2);

        List<UserResponseDto> result = userService.findAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(u -> u.getName().equals("User1")));
        assertTrue(result.stream().anyMatch(u -> u.getName().equals("User2")));
    }

    @Test
    void testUpdateUser() {
        User savedUser = userRepository.save(user);
        UserUpdateDto updateDto = new UserUpdateDto(
                savedUser.getId(),
                "newName",
                "newEmail@ya.ru"
        );

        UserResponseDto result = userService.updateUser(updateDto);

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals("newName", result.getName());
        assertEquals("newEmail@ya.ru", result.getEmail());

        Optional<User> updatedUser = userRepository.findById(savedUser.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals("newName", updatedUser.get().getName());
        assertEquals("newEmail@ya.ru", updatedUser.get().getEmail());
    }

    @Test
    void testUpdateUserWhenUserNotExists() {
        UserUpdateDto updateDto = new UserUpdateDto(10L, "Name", "email@ya.ru");

        assertThrows(NotFoundException.class, () -> userService.updateUser(updateDto));
    }

    @Test
    void testDeleteUser() {
        User savedUser = userRepository.save(user);
        long userId = savedUser.getId();

        userService.deleteUser(userId);

        Optional<User> deletedUser = userRepository.findById(userId);
        assertFalse(deletedUser.isPresent());
    }
}