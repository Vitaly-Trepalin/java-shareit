package ru.practicum.shareit.user.repositories;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserRepository {
    UserDto findByIdUser(long userId);

    Collection<UserDto> findAllUsers();

    UserDto createUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    void deleteUser(long userId);
}