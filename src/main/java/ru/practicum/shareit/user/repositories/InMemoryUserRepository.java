package ru.practicum.shareit.user.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ResourceConflictException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class InMemoryUserRepository implements UserRepository {
    private final UserMapper userMapper;
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public UserDto findByIdUser(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Нет пользователя с id = " + userId);
        }
        return userMapper.toUserDto(users.get(userId));
    }

    @Override
    public Collection<UserDto> findAllUsers() {
        return users.values().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        checkUniquenessOfFields(userDto);
        User user = userMapper.toUser(idGenerator, userDto);
        users.put(user.getId(), user);
        return userMapper.toUserDto(users.get(idGenerator.get() - 1));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Нет пользователя с id = " + userId);
        }
        User user = users.get(userId);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            checkUniquenessOfFields(userDto);
            user.setEmail(userDto.getEmail());
        }
        return userMapper.toUserDto(users.get(userId));
    }

    @Override
    public void deleteUser(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Нет пользователя с id = " + userId);
        }
        users.remove(userId);
    }

    private void checkUniquenessOfFields(UserDto userDto) {
        users.values().stream()
                .map(User::getEmail)
                .forEach(email -> {
                    if (email.equals(userDto.getEmail())) {
                        throw new ResourceConflictException(String.format("Пользователь с email = %s уже существует",
                                email));
                    }
                });
    }
}
