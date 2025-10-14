package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.validator.UserValidator;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<UserDto> findAll() {

        return repository.findAll()
                .stream()
                .map(user -> UserMapper.mapToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long userId) {

        User user = repository.findById(userId);
        if (user == null) {
            String bugText = "Пользователь не найден. id " + userId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto create(NewUserRequest newUser) {
        User user = UserMapper.mapToUser(newUser);

        if (repository.isEmailExist(user.getEmail(), null)) {
            String bugText = "Email " + user.getEmail() + " существует";
            log.warn(bugText);
            throw new ValidationException(bugText);
        }

        repository.create(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto update(UpdateUserRequest newUser) {
        Long id = newUser.getId();

        User userInMemory = repository.findById(id);
        if (userInMemory == null) {
            String bugText = "Пользователь не найден. id " + id;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }
        User updatedUser = UserMapper.updateUserFields(userInMemory, newUser);

        if (!UserValidator.isUserValid(updatedUser)) {
            String bugValidationText = "Ошибка валидации";
            log.warn(bugValidationText);
            throw new ValidationException(bugValidationText);
        }

        if (repository.isEmailExist(updatedUser.getEmail(), id)) {
            String bugEmailText = "Email " + updatedUser.getEmail() + "существует";
            log.warn(bugEmailText);
            throw new ValidationException(bugEmailText);
        }

        updatedUser = repository.update(updatedUser);

        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto delete(Long userId) {
        User userInMemory = repository.findById(userId);

        if (userInMemory == null) {
            String bugText = "Пользователь не найден. id " + userInMemory.getId();
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        repository.delete(userInMemory);

        return UserMapper.mapToUserDto(userInMemory);
    }

}
