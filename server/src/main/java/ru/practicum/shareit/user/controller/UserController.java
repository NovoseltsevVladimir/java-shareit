package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable long userId) {
        return userService.findById(userId);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserDto user) {
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UpdateUserDto newUser,
                          @PathVariable long userId) {
        newUser.setId(userId);
        return userService.update(newUser);
    }

    @DeleteMapping("/{userId}")
    public UserDto delete(@PathVariable("userId") Long userId) {
        return userService.delete(userId);
    }


}
