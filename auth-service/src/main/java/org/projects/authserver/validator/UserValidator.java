package org.projects.authserver.validator;


import lombok.RequiredArgsConstructor;
import org.projects.authserver.dto.CreateUpdateUserDTO;
import org.projects.authserver.model.User;
import org.projects.authserver.repository.UserRepository;
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
        CreateUpdateUserDTO user = (CreateUpdateUserDTO) target;

        userRepository.findByLogin(user.login())
                .ifPresent(user1 -> errors.rejectValue(
                        "login",
                        "user.login.exists",
                        "User with this login already exists"));

        userRepository.findByEmail(user.email())
                .ifPresent(user1 -> errors.rejectValue(
                        "email",
                        "user.email.exists",
                        "User with this email already exists"));


        userRepository.findByPhone(user.phone())
                .ifPresent(user1 -> errors.rejectValue(
                        "phone",
                        "user.phone.exists",
                        "User with this phone already exists"));
    }
}
