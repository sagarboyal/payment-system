package com.ps.backend.payment.processor;

import com.ps.backend.payment.processor.dto.PaymentProcessorRequest;
import com.ps.backend.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);
}
