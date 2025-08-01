package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;

public interface UserService {
    UserResponseDto findByIdUser(long userId);

    Collection<UserResponseDto> findAllUsers();

    UserResponseDto createUser(UserCreateDto userCreateDto);

    UserResponseDto updateUser(UserUpdateDto userUpdateDto);

    void deleteUser(long userId);
}