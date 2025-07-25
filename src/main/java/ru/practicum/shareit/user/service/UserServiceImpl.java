package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto findByIdUser(long userId) {
        log.info("method launched (findByIdUser(long userId))");
        return userRepository.findByIdUser(userId);
    }

    @Override
    public Collection<UserDto> findAllUsers() {
        log.info("method launched (findAllUsers())");
        return userRepository.findAllUsers();
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("method launched (createUser(UserDto userDto))");
        return userRepository.createUser(userDto);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        log.info("method launched (updateUser(long userId, User user))");
        return userRepository.updateUser(userId, userDto);
    }

    @Override
    public void deleteUser(long userId) {
        log.info("method launched (deleteUser(long userId))");
        userRepository.deleteUser(userId);
    }
}
