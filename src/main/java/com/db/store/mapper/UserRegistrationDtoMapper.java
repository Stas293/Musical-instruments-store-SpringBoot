package com.db.store.mapper;

import com.db.store.dto.CreateUpdateUserDTO;
import com.db.store.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserRegistrationDtoMapper {
    User toEntity(CreateUpdateUserDTO createUpdateUserDTO);

    CreateUpdateUserDTO toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(CreateUpdateUserDTO createUpdateUserDTO, @MappingTarget User user);
}