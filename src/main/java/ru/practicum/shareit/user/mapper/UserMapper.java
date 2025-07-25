package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public User toUser(AtomicLong idGenerator, UserDto userDto) {
        return new User(idGenerator.getAndIncrement(), userDto.getName(), userDto.getEmail());
    }
}
