package ru.practicum.shareit.user.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
@Slf4j
public class InMemoryUserRepository implements UserRepository {
    private final UserMapper userMapper;
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public User findByIdUser(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Нет пользователя с id = " + userId);
        }
        return users.get(userId);
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        User newUser = User.builder()
                .id(idGenerator.getAndIncrement())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void deleteUser(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Нет пользователя с id = " + userId);
        }
        users.remove(userId);
    }
}