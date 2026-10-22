package com.ps.backend.payment.processor.dto;

import com.ps.backend.common.entity.Money;
import com.ps.backend.common.enums.PaymentMethod;

import java.util.Map;

public record PaymentProcessorRequest(
        PaymentMethod method,
        Money amount,
        Map<String, Object> methodDetails
) {
}
