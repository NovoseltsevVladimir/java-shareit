package ru.practicum.shareit.user.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ru.practicum.shareit.user.model.User;

import java.util.Set;

public class UserValidator {

    private static Validator validator;

    public static boolean isUserValid(User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        return violations.isEmpty();
    }
}
