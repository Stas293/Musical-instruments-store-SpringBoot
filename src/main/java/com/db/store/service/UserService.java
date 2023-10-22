package com.db.store.service;

import com.db.store.dto.*;
import com.db.store.model.User;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface UserService {
    JwtResponseDTO login(JwtRequestDTO jwtRequestDto);

    UserDTO register(CreateUpdateUserDTO user, BindingResult errors);

    Optional<User> getUserByLogin(String login);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByPhone(String phone);

    UserDTO disableUser(Long id);

    UserDTO enableUser(Long id);

    UserDTO updateUserRoles(Long id, List<RoleDTO> roleList);

    UserDTO updateUserData(CreateUpdateUserDTO updatedUserData, BindingResult errors);

    Page<UserDTO> getAllUsers(int page, int size);

    UserDTO getUserById(Long id);

    @Transactional
    void deleteUser(Long id);
}
