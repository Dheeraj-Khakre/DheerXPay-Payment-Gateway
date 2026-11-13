package com.dheerxpay.merchant.service;

import com.dheerxpay.merchant.dto.request.CreateApiKeyRequest;
import com.dheerxpay.merchant.dto.response.ApiKeyCreateResponse;
import com.dheerxpay.merchant.dto.response.ApiKeyResponse;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse createApiKey(UUID merchantId, CreateApiKeyRequest apiKeyRequest);

    List<ApiKeyResponse> getApiKeyListByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}
