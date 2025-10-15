package ru.practicum.shareit.user.validator;

import jakarta.validation.*;
import ru.practicum.shareit.user.model.User;

import java.util.Set;

public class UserValidator {

    private static Validator validator;

    public static void validateUser(User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            throw new ValidationException(violations.toString());
        }
    }
}
