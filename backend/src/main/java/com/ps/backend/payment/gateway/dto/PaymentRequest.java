package com.ps.backend.payment.gateway.dto;

import com.ps.backend.common.entity.Money;
import com.ps.backend.common.enums.PaymentMethod;
import tools.jackson.databind.ser.jdk.UUIDSerializer;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest (
        UUID paymentId,
        UUID orderId,
        UUID amount,
        Money money,
        PaymentMethod method,
        Map<String, Object> methodDetails
) {
}
