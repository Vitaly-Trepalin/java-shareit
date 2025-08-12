package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findByIdUser(@PathVariable @Positive long userId) {
        log.info("Method launched (findByIdUser(long userId = {}))", userId);
        return new ResponseEntity<>(userService.findByIdUser(userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<UserResponseDto>> findAllUsers() {
        log.info("Method launched (findAllUsers())");
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        log.info("Method launched (createUser(UserDto userDto = {}))", userCreateDto);
        return new ResponseEntity<>(userService.createUser(userCreateDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable @Positive long userId,
                                                      @RequestBody @Valid UserUpdateDto userUpdateDto) {
        log.info("Method launched (updateUser(long userId = {}, User user = {}))", userId, userUpdateDto);
        UserUpdateDto newUserUpdateDto = new UserUpdateDto(userId, userUpdateDto.getName(), userUpdateDto.getEmail());
        return new ResponseEntity<>(userService.updateUser(newUserUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive long userId) {
        log.info("Method launched (deleteUser(long userId = {}))", userId);
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}