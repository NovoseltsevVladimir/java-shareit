package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> findAll();

    UserDto findById(Long userId);

    UserDto create(NewUserRequest user);

    UserDto update(UpdateUserRequest user);

    UserDto delete(Long userId);
}
