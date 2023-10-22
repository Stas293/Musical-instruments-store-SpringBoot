package com.db.store.mapper;

import com.db.store.dto.UserDTO;
import com.db.store.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDtoMapper {
    User toEntity(UserDTO userDTO);

    UserDTO toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDTO userDTO, @MappingTarget User user);
}