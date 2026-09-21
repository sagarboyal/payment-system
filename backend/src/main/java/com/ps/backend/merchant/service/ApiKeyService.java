package com.ps.backend.merchant.service;

import com.ps.backend.merchant.dto.request.CreateApiKeyRequest;
import com.ps.backend.merchant.dto.response.ApiKeyCreateResponse;
import com.ps.backend.merchant.dto.response.ApiKeyResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse create(UUID merchantId, @Valid CreateApiKeyRequest request);
    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);
}
