package com.ps.backend.merchant.mapper;

import com.ps.backend.merchant.dto.response.ApiKeyCreateResponse;
import com.ps.backend.merchant.dto.response.ApiKeyResponse;
import com.ps.backend.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {
    ApiKeyCreateResponse toCreateResponse(ApiKey apiKey);
    List<ApiKeyResponse> toResponseList(List<ApiKey> apiKeyList);
}
