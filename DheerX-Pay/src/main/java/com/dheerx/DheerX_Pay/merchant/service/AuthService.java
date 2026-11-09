package com.dheerx.DheerX_Pay.merchant.service;

import com.dheerx.DheerX_Pay.merchant.dto.request.MerchantSignUpRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {
    MerchantResponse signUp( MerchantSignUpRequest request);
}
