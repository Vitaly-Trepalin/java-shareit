package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserResponseDto findByIdUser(@PathVariable long userId) {
        log.info("Method launched (findByIdUser(long userId = {}))", userId);
        return userService.findByIdUser(userId);
    }

    @GetMapping
    public List<UserResponseDto> findAllUsers() {
        log.info("Method launched (findAllUsers())");
        return userService.findAllUsers();
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserCreateDto userCreateDto) {
        log.info("Method launched (createUser(UserDto userDto = {}))", userCreateDto);
        return userService.createUser(userCreateDto);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(@PathVariable long userId,
                                      @RequestBody UserUpdateDto userUpdateDto) {
        log.info("Method launched (updateUser(long userId = {}, User user = {}))", userId, userUpdateDto);
        UserUpdateDto newUserUpdateDto = new UserUpdateDto(userId, userUpdateDto.getName(), userUpdateDto.getEmail());
        return userService.updateUser(newUserUpdateDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Method launched (deleteUser(long userId = {}))", userId);
        userService.deleteUser(userId);
    }
}