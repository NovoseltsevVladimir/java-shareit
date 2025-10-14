package ru.practicum.shareit.user.mapper;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

@Component
@NoArgsConstructor
public class UserMapper {

    public static User mapToUser(NewUserRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        return user;
    }

    public static UserDto mapToUserDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());

        return userDto;
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {

        if (request.hasName()) {
            user.setName(request.getName());
        }

        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }

        return user;
    }
}
