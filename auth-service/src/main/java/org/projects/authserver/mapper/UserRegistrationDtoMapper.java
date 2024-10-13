package org.projects.authserver.mapper;

import org.mapstruct.*;
import org.projects.authserver.dto.CreateUpdateUserDTO;
import org.projects.authserver.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserRegistrationDtoMapper {
    User toEntity(CreateUpdateUserDTO createUpdateUserDTO);

    CreateUpdateUserDTO toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(CreateUpdateUserDTO createUpdateUserDTO, @MappingTarget User user);
}