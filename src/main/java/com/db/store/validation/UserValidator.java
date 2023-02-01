package com.db.store.validation;

import com.db.store.model.User;
import com.db.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (userService.getUserByLogin(user.getLogin()).isPresent()) {
            errors.rejectValue("login", "login.taken", "validation.login.taken");
        }

        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "email.taken", "validation.email.taken");
        }

        if (userService.getUserByPhone(user.getPhone()).isPresent()) {
            errors.rejectValue("phone", "phone.taken", "validation.phone.taken");
        }
    }
}
