package com.db.store.controller;

import com.db.store.dto.RoleDTO;
import com.db.store.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/role")
@AllArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<RoleDTO> getRoles(@RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "5") int size) {
        return roleService.getAllRoles(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleDTO getRole(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDTO createRole(@RequestBody @Validated RoleDTO roleDTO,
                              BindingResult bindingResult) {
        return roleService.saveRole(roleDTO, bindingResult);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleDTO updateRole(@PathVariable Long id,
                              @RequestBody @Validated RoleDTO roleDTO,
                              BindingResult bindingResult) {
        return roleService.updateRoleById(id, roleDTO, bindingResult);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRoleById(id);
    }
}
