package com.ps.backend.payment.gateway;

import com.ps.backend.payment.gateway.dto.PaymentRequest;
import com.ps.backend.payment.gateway.dto.PaymentResult;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest request);
}
