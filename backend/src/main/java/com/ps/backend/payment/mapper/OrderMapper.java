package com.ps.backend.payment.mapper;

import com.ps.backend.payment.dto.response.OrderResponse;
import com.ps.backend.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderResponse toResponse(OrderRecord orderRecord);
}
