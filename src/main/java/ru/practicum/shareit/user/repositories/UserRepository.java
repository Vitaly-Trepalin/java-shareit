package ru.practicum.shareit.user.repositories;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    User findByIdUser(long userId);

    Collection<User> findAllUsers();

    User createUser(User user);

    void updateUser(User user);

    void deleteUser(long userId);
}