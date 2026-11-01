package com.ps.backend.merchant.dto.request;

import com.ps.backend.common.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}
