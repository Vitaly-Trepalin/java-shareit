package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        log.info("Method launched (createUser(UserDto userDto = {}))", userCreateDto);
        return userClient.createUser(userCreateDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        log.info("Method launched (findAllUsers())");
        return userClient.findAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findByIdUser(@PathVariable @Positive long userId) {
        log.info("Method launched (findByIdUser(long userId = {}))", userId);
        return userClient.findByIdUser(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable @Positive long userId,
                                             @RequestBody @Valid UserUpdateDto userUpdateDto) {
        log.info("Method launched (updateUser(long userId = {}, User user = {}))", userId, userUpdateDto);
        return userClient.updateUser(userId, userUpdateDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Positive long userId) {
        log.info("Method launched (deleteUser(long userId = {}))", userId);
        userClient.deleteUser(userId);
    }
}