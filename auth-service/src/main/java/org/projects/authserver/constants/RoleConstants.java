package org.projects.authserver.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleConstants {
    ROLE_NOT_FOUND("Role not found"),
    ROLE_NAME_EXISTS("role.name.exists"),
    ROLE_CODE_EXISTS("role.code.exists"),
    ROLE_FIELD_EXISTS("role.%s.exists");

    private final String message;
}
