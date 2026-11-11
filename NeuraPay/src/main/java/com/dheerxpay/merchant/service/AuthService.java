package com.dheerxpay.merchant.service;

import com.dheerxpay.merchant.dto.request.MerchantSignUpRequest;
import com.dheerxpay.merchant.dto.response.MerchantResponse;

public interface AuthService {
    MerchantResponse signUp( MerchantSignUpRequest request);
}
