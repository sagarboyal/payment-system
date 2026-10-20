package com.ps.backend.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ps.backend.common.entity.Money;
import com.ps.backend.common.enums.PaymentMethod;
import com.ps.backend.common.enums.PaymentStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        UUID id,
        UUID orderId,
        Money amount,
        PaymentStatus status,
        PaymentMethod method,
        Map<String, Object> methodDetails,
        String errorCode,
        String errorDescription,
        LocalDateTime capturedAt,
        LocalDateTime createdAt
) {
}
