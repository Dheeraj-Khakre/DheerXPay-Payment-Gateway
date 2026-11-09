package com.dheerx.DheerX_Pay.merchant.service.impl;

import com.dheerx.DheerX_Pay.common.enums.MerchantStatus;
import com.dheerx.DheerX_Pay.common.enums.UserRoles;
import com.dheerx.DheerX_Pay.common.exceptions.DuplicateResourceException;
import com.dheerx.DheerX_Pay.common.utility.RandomizerUtil;
import com.dheerx.DheerX_Pay.merchant.dto.request.MerchantSignUpRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.MerchantResponse;
import com.dheerx.DheerX_Pay.merchant.entities.AppUser;
import com.dheerx.DheerX_Pay.merchant.entities.Merchant;
import com.dheerx.DheerX_Pay.merchant.repository.AppUserRespository;
import com.dheerx.DheerX_Pay.merchant.repository.MerchantRepository;
import com.dheerx.DheerX_Pay.merchant.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl  implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRespository appUserRespository;




    @Override
    @Transactional
    public MerchantResponse signUp(MerchantSignUpRequest request) {
        if(merchantRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL","merchant has already exists for given email:"+request.email());
        }
        Merchant merchant = Merchant.builder()
                .businessName(request.businessName())
                .email(request.email())
                .legalName(request.name())
                .status(MerchantStatus.PENDING_KYC)
                .businessType(request.businessType())
                .merchantCode(RandomizerUtil.generate())
                .phone(request.phone())
                .build();
        Merchant savedMerchant = merchantRepository.save(merchant);

        AppUser user = AppUser.builder()
                .email(request.email())
                .passwordHash(request.password())
                .merchant(savedMerchant)
                .role(UserRoles.OWNER)
                .build();
        appUserRespository.save(user);
        return MerchantResponse.builder()
                .id(savedMerchant.getId())
                .businessName(savedMerchant.getBusinessName())
                .email(savedMerchant.getEmail())
                .status(savedMerchant.getStatus())
                .businessType(savedMerchant.getBusinessType())
                .kycVerified(savedMerchant.isKycVerified())
                .phone(savedMerchant.getPhone())
                .merchantCode(savedMerchant.getMerchantCode())
                .build();

    }
}
