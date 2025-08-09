package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

@Component
public class UserMapper {
    public UserResponseDto mapToUserDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }

    public User mapToUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setName(userCreateDto.getName());
        user.setEmail(userCreateDto.getEmail());
        return user;
    }
}