package com.dheerx.DheerX_Pay.merchant.service;

import com.dheerx.DheerX_Pay.merchant.dto.request.CreateApiKeyRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.ApiKeyCreateResponse;
import com.dheerx.DheerX_Pay.merchant.dto.response.ApiKeyResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse createApiKey(UUID merchantId, CreateApiKeyRequest apiKeyRequest);

    List<ApiKeyResponse> getApiKeyListByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}
