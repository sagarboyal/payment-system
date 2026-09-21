package com.ps.backend.merchant.dto.response;

import com.ps.backend.common.enums.BusinessType;
import com.ps.backend.common.enums.MerchantStatus;
import lombok.Data;

import java.util.UUID;


public record MerchantResponse(
        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus
) {
}
