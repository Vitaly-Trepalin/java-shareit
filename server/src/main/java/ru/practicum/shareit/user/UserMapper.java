package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

public class UserMapper {
    public static UserResponseDto mapToUserDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User mapToUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setName(userCreateDto.getName());
        user.setEmail(userCreateDto.getEmail());
        return user;
    }
}