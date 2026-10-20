package com.ps.backend.merchant.mapper;

import com.ps.backend.merchant.dto.request.MerchantSignupRequest;
import com.ps.backend.merchant.dto.response.MerchantResponse;
import com.ps.backend.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {
    Merchant toEntityFromSignupRequest(MerchantSignupRequest request);
    MerchantResponse toResponse(Merchant merchant);
}
