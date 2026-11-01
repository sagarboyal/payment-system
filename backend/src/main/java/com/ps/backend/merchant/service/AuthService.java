package com.ps.backend.merchant.service;

import com.ps.backend.merchant.dto.request.MerchantSignupRequest;
import com.ps.backend.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {
    MerchantResponse signup( MerchantSignupRequest request);
}
