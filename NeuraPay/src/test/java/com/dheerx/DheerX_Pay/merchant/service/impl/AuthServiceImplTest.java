package com.dheerx.DheerX_Pay.merchant.service.impl;

import com.dheerx.DheerX_Pay.common.enums.MerchantStatus;
import com.dheerx.DheerX_Pay.common.enums.UserRoles;
import com.dheerx.DheerX_Pay.common.exceptions.DuplicateResourceException;
import com.dheerx.DheerX_Pay.merchant.dto.request.MerchantSignUpRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.MerchantResponse;
import com.dheerx.DheerX_Pay.merchant.entities.AppUser;
import com.dheerx.DheerX_Pay.merchant.entities.Merchant;
import com.dheerx.DheerX_Pay.merchant.mapper.MerchantMapper;
import com.dheerx.DheerX_Pay.merchant.repository.AppUserRespository;
import com.dheerx.DheerX_Pay.merchant.repository.MerchantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private AppUserRespository appUserRespository;
    @Mock
    private MerchantMapper merchantMapper;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void signUpCreatesMerchantOwnerAndResponse() {
        MerchantSignUpRequest request = new MerchantSignUpRequest(
                "Example Store", com.dheerx.DheerX_Pay.common.enums.BusinessType.PRIVATE_LIMITED,
                "Example Legal", "owner@example.com", "password123", "1234567890"
        );
        Merchant merchant = new Merchant();
        Merchant savedMerchant = new Merchant();
        MerchantResponse response = MerchantResponse.builder().email(request.email()).build();
        when(merchantRepository.existsByEmail(request.email())).thenReturn(false);
        when(merchantMapper.toEntity(eq(request), eq(MerchantStatus.PENDING_KYC), any()))
                .thenReturn(merchant);
        when(merchantRepository.save(merchant)).thenReturn(savedMerchant);
        when(merchantMapper.toAppUser(request, savedMerchant, UserRoles.OWNER)).thenReturn(new AppUser());
        when(merchantMapper.toResponse(savedMerchant)).thenReturn(response);

        MerchantResponse actual = authService.signUp(request);

        assertSame(response, actual);
        verify(merchantMapper).toAppUser(request, savedMerchant, UserRoles.OWNER);
        verify(appUserRespository).save(any(AppUser.class));
    }

    @Test
    void signUpRejectsDuplicateEmailBeforeSaving() {
        MerchantSignUpRequest request = new MerchantSignUpRequest(
                "Example Store", com.dheerx.DheerX_Pay.common.enums.BusinessType.PRIVATE_LIMITED,
                "Example Legal", "owner@example.com", "password123", "1234567890"
        );
        when(merchantRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.signUp(request));

        verify(merchantMapper, never()).toEntity(any(), any(), any());
        verify(merchantRepository, never()).save(any());
        verify(appUserRespository, never()).save(any());
    }
}
