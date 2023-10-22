package com.db.store.mapper;

import com.db.store.dto.OrderHistoryDTO;
import com.db.store.model.OrderHistory;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderHistoryDtoMapper {
    OrderHistory toEntity(OrderHistoryDTO orderHistoryDTO);

    OrderHistoryDTO toDto(OrderHistory orderHistory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderHistory partialUpdate(OrderHistoryDTO orderHistoryDTO, @MappingTarget OrderHistory orderHistory);
}