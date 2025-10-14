package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {

    Collection<User> findAll();

    User create(User user);

    User update(User user);

    User delete(User user);

    User findById(Long id);

    boolean isEmailExist(String email, Long userId);

    boolean isUserExist(Long userId);
}
