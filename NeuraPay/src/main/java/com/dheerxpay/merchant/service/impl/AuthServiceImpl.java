package com.dheerxpay.merchant.service.impl;

import com.dheerxpay.common.enums.MerchantStatus;
import com.dheerxpay.common.enums.UserRoles;
import com.dheerxpay.common.exceptions.DuplicateResourceException;
import com.dheerxpay.common.utility.RandomizerUtil;
import com.dheerxpay.merchant.dto.request.MerchantSignUpRequest;
import com.dheerxpay.merchant.dto.response.MerchantResponse;
import com.dheerxpay.merchant.entities.Merchant;
import com.dheerxpay.merchant.mapper.MerchantMapper;
import com.dheerxpay.merchant.repository.AppUserRespository;
import com.dheerxpay.merchant.repository.MerchantRepository;
import com.dheerxpay.merchant.service.AuthService;
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
