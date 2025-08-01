package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ResourceConflictException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto findByIdUser(long userId) {
        User user = userRepository.findByIdUser(userId);
        return userMapper.toUserDto(user);
    }

    @Override
    public Collection<UserResponseDto> findAllUsers() {
        Collection<User> users = userRepository.findAllUsers();
        return users.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        User user = userMapper.toUser(userCreateDto);
        boolean isExistsUser = userRepository.findAllUsers().stream().
                anyMatch(user1 -> user1.getEmail().equals(user.getEmail()));
        if (isExistsUser) {
            throw new ResourceConflictException(String.format("Пользователь с email = %s уже " +
                    "существует", user.getEmail()));
        }
        User userSaved = userRepository.createUser(user);
        return userMapper.toUserDto(userSaved);
    }

    @Override
    public UserResponseDto updateUser(UserUpdateDto userUpdateDto) {
        User updatedUser = userRepository.findByIdUser(userUpdateDto.getId());
        if (userUpdateDto.getName() != null) {
            updatedUser.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            userRepository.findAllUsers().stream()
                    .map(User::getEmail)
                    .forEach(email -> {
                        if (email.equals(userUpdateDto.getEmail())) {
                            throw new ResourceConflictException(String.format("Пользователь с email = %s уже " +
                                            "существует", email));
                        }
                    });
            updatedUser.setEmail(userUpdateDto.getEmail());
        }
        userRepository.updateUser(updatedUser);
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteUser(userId);
    }
}