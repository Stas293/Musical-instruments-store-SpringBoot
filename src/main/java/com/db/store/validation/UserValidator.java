package com.db.store.validation;

import com.db.store.model.User;
import com.db.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        userRepository.findByLogin(user.getLogin())
                .ifPresent(user1 -> errors.rejectValue(
                        "login",
                        "User with this login already exists",
                        "validation.user.login.exists"));

        userRepository.findByEmail(user.getEmail())
                .ifPresent(user1 -> errors.rejectValue(
                        "email",
                        "User with this email already exists",
                        "validation.user.email.exists"));

        userRepository.findByPhone(user.getPhone())
                .ifPresent(user1 -> errors.rejectValue(
                        "phone",
                        "User with this phone already exists",
                        "validation.user.phone.exists"));
    }
}
