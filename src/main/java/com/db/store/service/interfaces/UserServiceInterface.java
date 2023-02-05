package com.db.store.service.interfaces;

import com.db.store.model.Role;
import com.db.store.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    User register(User user);

    Optional<User> getUserByLogin(String login);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByPhone(String phone);

    User disableUser(Long id);

    User enableUser(Long id);

    User updateUserRoles(Long id, List<Role> roleList);

    User updateUserData(User user);

    Page<User> getAllUsers(int page, int size);

    User getUserById(Long id);

    @Transactional
    User deleteUser(Long id);
}
