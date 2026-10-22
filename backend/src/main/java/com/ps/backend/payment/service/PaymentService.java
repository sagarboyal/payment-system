package com.ps.backend.payment.service;

import com.ps.backend.payment.dto.request.PaymentInitRequest;
import com.ps.backend.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);
}
