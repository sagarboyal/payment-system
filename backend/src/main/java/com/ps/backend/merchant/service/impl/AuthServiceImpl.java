package com.ps.backend.merchant.service.impl;

import com.ps.backend.common.enums.MerchantStatus;
import com.ps.backend.common.enums.Role;
import com.ps.backend.common.exception.DuplicateResourceException;
import com.ps.backend.merchant.dto.request.MerchantSignupRequest;
import com.ps.backend.merchant.dto.response.MerchantResponse;
import com.ps.backend.merchant.entity.AppUser;
import com.ps.backend.merchant.entity.Merchant;
import com.ps.backend.merchant.mapper.MerchantMapper;
import com.ps.backend.merchant.repository.AppUserRepository;
import com.ps.backend.merchant.repository.MerchantRepository;
import com.ps.backend.merchant.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private  final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {
        if (merchantRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL" ,
                    "Merchant with email already exists: "+request.email());
        }
        Merchant merchant = merchantMapper.toEntityFromSignupRequest(request);
        merchant.setStatus(MerchantStatus.PENDING_KYC);
        merchant = merchantRepository.save(merchant);

//        Merchant merchant = Merchant.builder()
//                .businessName((request.businessName()))
//                .businessType(request.businessType())
//                .name(request.name())
//                .email(request.email())
//                .status(MerchantStatus.PENDING_KYC)
//                .build();

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .password(request.password()) //TODO: encrypt using Bcrypt
                .role(Role.OWNER)
                .build();
        appUserRepository.save(appUser);

        return merchantMapper.toResponse(merchant);
    }
}
