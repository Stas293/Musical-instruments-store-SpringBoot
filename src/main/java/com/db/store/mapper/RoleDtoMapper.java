package com.db.store.mapper;

import com.db.store.dto.RoleDTO;
import com.db.store.model.Role;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleDtoMapper {
    Role toEntity(RoleDTO roleDTO);

    RoleDTO toDto(Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Role partialUpdate(RoleDTO roleDTO, @MappingTarget Role role);
}