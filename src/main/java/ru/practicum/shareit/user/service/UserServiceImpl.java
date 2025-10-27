package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.validator.UserValidator;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
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

        User user = repository.findById(userId).get();

        if (user == null) {
            String bugText = "Пользователь не найден. id " + userId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto create(NewUserDto newUser) {
        User user = UserMapper.mapToUser(newUser);

        if (isEmailExist(user.getEmail(), null)) {
            String bugText = "Email " + user.getEmail() + " существует";
            log.warn(bugText);
            throw new ValidationException(bugText);
        }

        user = repository.save(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto update(UpdateUserDto newUser) {
        Long id = newUser.getId();

        User userInMemory = repository.findById(id).get();
        if (userInMemory == null) {
            String bugText = "Пользователь не найден. id " + id;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }
        User updatedUser = UserMapper.updateUserFields(userInMemory, newUser);

       UserValidator.validateUser(updatedUser);

        if (isEmailExist(updatedUser.getEmail(), id)) {
            String bugEmailText = "Email " + updatedUser.getEmail() + "существует";
            log.warn(bugEmailText);
            throw new ValidationException(bugEmailText);
        }

        updatedUser = repository.save(updatedUser);

        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto delete(Long userId) {
        User userInMemory = repository.findById(userId).get();

        if (userInMemory == null) {
            String bugText = "Пользователь не найден. id " + userInMemory.getId();
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        repository.delete(userInMemory);

        return UserMapper.mapToUserDto(userInMemory);
    }

    public User findUserById (Long userId) {
        User user = repository.findById(userId).get();
        if (user == null) {
            String bugText = "Пользователь не найден. id " + userId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        return user;
    }

    public boolean isEmailExist(String email, Long userId) {
        return repository.findAll()
                .stream()
                .filter(user -> (!user.getId().equals(userId)))
                .map(user -> user.getEmail())
                .filter(currentEmail -> currentEmail.equals(email))
                .collect(Collectors.toList()).size() > 0;
    }

    public boolean isUserExist(Long id) {
        return repository.findById(id).isPresent();
    }

}
