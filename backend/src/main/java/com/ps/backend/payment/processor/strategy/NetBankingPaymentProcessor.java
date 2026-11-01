package com.ps.backend.payment.processor.strategy;

import com.ps.backend.payment.processor.PaymentProcessor;
import com.ps.backend.payment.processor.dto.PaymentProcessorRequest;
import com.ps.backend.payment.processor.dto.PaymentProcessorResponse;

public class NetBankingPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }
}
