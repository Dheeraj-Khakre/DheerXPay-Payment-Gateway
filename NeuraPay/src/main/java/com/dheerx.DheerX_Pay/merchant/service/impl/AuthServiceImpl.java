package com.dheerx.DheerX_Pay.merchant.service.impl;

import com.dheerx.DheerX_Pay.common.enums.MerchantStatus;
import com.dheerx.DheerX_Pay.common.enums.UserRoles;
import com.dheerx.DheerX_Pay.common.exceptions.DuplicateResourceException;
import com.dheerx.DheerX_Pay.common.utility.RandomizerUtil;
import com.dheerx.DheerX_Pay.merchant.dto.request.MerchantSignUpRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.MerchantResponse;
import com.dheerx.DheerX_Pay.merchant.entities.Merchant;
import com.dheerx.DheerX_Pay.merchant.mapper.MerchantMapper;
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
    private final MerchantMapper merchantMapper;


    @Override
    @Transactional
    public MerchantResponse signUp(MerchantSignUpRequest request) {
        if(merchantRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL","merchant has already exists for given email:"+request.email());
        }
        Merchant merchant = merchantMapper.toEntity(
                request,
                MerchantStatus.PENDING_KYC,
                RandomizerUtil.generate()
        );
        Merchant savedMerchant = merchantRepository.save(merchant);

        var user = merchantMapper.toAppUser(request, savedMerchant, UserRoles.OWNER);
        appUserRespository.save(user);
        return merchantMapper.toResponse(savedMerchant);

    }
}
