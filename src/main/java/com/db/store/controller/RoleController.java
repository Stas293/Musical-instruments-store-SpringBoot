package com.db.store.controller;

import com.db.store.dto.RoleDTO;
import com.db.store.model.Role;
import com.db.store.service.RoleService;
import com.db.store.utils.ObjectMapper;
import com.db.store.validation.RoleValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/role")
public class RoleController {
    private final RoleService roleService;
    private final ObjectMapper objectMapper;
    private final RoleValidator roleValidator;

    @Autowired
    public RoleController(RoleService roleService,
                          ObjectMapper objectMapper,
                          RoleValidator roleValidator) {
        this.roleService = roleService;
        this.objectMapper = objectMapper;
        this.roleValidator = roleValidator;
    }

    @GetMapping()
    public ResponseEntity<List<RoleDTO>> getRoles(@RequestParam(required = false, defaultValue = "0") int page,
                                                  @RequestParam(required = false, defaultValue = "5") int size) {
        return ResponseEntity.ok().body(
                objectMapper.mapList(
                        roleService.getAllRoles(page, size).getContent(), RoleDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                objectMapper.map(
                        roleService.getRole(id), RoleDTO.class));
    }

    @PostMapping()
    public ResponseEntity<RoleDTO> createRole(@RequestBody @Valid RoleDTO roleDTO,
                                              BindingResult bindingResult) throws MethodArgumentNotValidException {
        Role role = objectMapper.map(roleDTO, Role.class);
        roleValidator.validate(role, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(
                    new MethodParameter(this.getClass().getDeclaredMethods()[0], 0),
                    bindingResult
            );
        }
        return ResponseEntity.ok().body(
                objectMapper.map(
                        roleService.saveRole(role), RoleDTO.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id,
                                              @RequestBody @Valid RoleDTO roleDTO) {
        Role role = objectMapper.map(roleDTO, Role.class);
        return ResponseEntity.ok().body(
                objectMapper.map(
                        roleService.updateRoleById(id, role), RoleDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoleDTO> deleteRole(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                objectMapper.map(
                        roleService.deleteRoleById(id), RoleDTO.class));
    }
}
