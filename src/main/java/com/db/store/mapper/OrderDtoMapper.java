package com.db.store.mapper;

import com.db.store.dto.OrderDTO;
import com.db.store.model.Order;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderDtoMapper {
    Order toEntity(OrderDTO orderDTO);

    @AfterMapping
    default void linkInstrumentOrders(@MappingTarget Order order) {
        order.getInstrumentOrders().forEach(instrumentOrder -> instrumentOrder.setOrder(order));
    }

    OrderDTO toDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(OrderDTO orderDTO, @MappingTarget Order order);
}