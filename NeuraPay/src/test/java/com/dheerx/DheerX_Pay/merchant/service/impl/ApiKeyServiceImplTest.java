package com.dheerx.DheerX_Pay.merchant.service.impl;

import com.dheerx.DheerX_Pay.common.enums.Environment;
import com.dheerx.DheerX_Pay.common.exceptions.ResourceNotFoundException;
import com.dheerx.DheerX_Pay.merchant.dto.request.CreateApiKeyRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.ApiKeyCreateResponse;
import com.dheerx.DheerX_Pay.merchant.dto.response.ApiKeyResponse;
import com.dheerx.DheerX_Pay.merchant.entities.ApiKey;
import com.dheerx.DheerX_Pay.merchant.entities.Merchant;
import com.dheerx.DheerX_Pay.merchant.mapper.ApiKeyMapper;
import com.dheerx.DheerX_Pay.merchant.repository.ApiKeyRepository;
import com.dheerx.DheerX_Pay.merchant.repository.MerchantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiKeyServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private ApiKeyRepository apiKeyRepository;
    @Mock
    private ApiKeyMapper apiKeyMapper;
    @InjectMocks
    private ApiKeyServiceImpl apiKeyService;

    @Test
    void createApiKeyMapsAndPersistsKey() {
        UUID merchantId = UUID.randomUUID();
        Merchant merchant = new Merchant();
        ApiKey apiKey = new ApiKey();
        ApiKeyCreateResponse response = ApiKeyCreateResponse.builder().build();
        CreateApiKeyRequest request = new CreateApiKeyRequest(Environment.SANDBOX);
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(merchant));
        when(apiKeyMapper.toEntity(any(), any(), anyString(), anyString())).thenReturn(apiKey);
        when(apiKeyRepository.save(apiKey)).thenReturn(apiKey);
        when(apiKeyMapper.toCreateResponse(any(), anyString())).thenReturn(response);

        assertSame(response, apiKeyService.createApiKey(merchantId, request));

        verify(apiKeyMapper).toEntity(eq(request), eq(merchant), anyString(), anyString());
        verify(apiKeyRepository).save(apiKey);
    }

    @Test
    void createApiKeyRejectsUnknownMerchant() {
        UUID merchantId = UUID.randomUUID();
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> apiKeyService.createApiKey(merchantId, new CreateApiKeyRequest(Environment.TEST)));

        verify(apiKeyRepository, never()).save(any());
    }

    @Test
    void listsMappedApiKeys() {
        UUID merchantId = UUID.randomUUID();
        ApiKey key = new ApiKey();
        ApiKeyResponse response = new ApiKeyResponse(
                UUID.randomUUID(), "key-id", Environment.TEST, true, null, null
        );
        when(apiKeyRepository.findByMerchant_Id(merchantId)).thenReturn(List.of(key));
        when(apiKeyMapper.toResponse(key)).thenReturn(response);

        assertSame(response, apiKeyService.getApiKeyListByMerchant(merchantId).getFirst());
    }

    @Test
    void revokeDisablesOwnedKey() {
        UUID merchantId = UUID.randomUUID();
        UUID keyId = UUID.randomUUID();
        Merchant merchant = Merchant.builder().id(merchantId).build();
        ApiKey key = ApiKey.builder().id(keyId).merchant(merchant).enabled(true).build();
        when(apiKeyRepository.findById(keyId)).thenReturn(Optional.of(key));

        apiKeyService.revoke(merchantId, keyId);

        assertFalse(key.isEnabled());
    }

    @Test
    void revokeRejectsKeyOwnedByAnotherMerchant() {
        UUID requestedMerchant = UUID.randomUUID();
        UUID keyId = UUID.randomUUID();
        ApiKey key = ApiKey.builder().merchant(Merchant.builder().id(UUID.randomUUID()).build()).build();
        when(apiKeyRepository.findById(keyId)).thenReturn(Optional.of(key));

        assertThrows(ResourceNotFoundException.class, () -> apiKeyService.revoke(requestedMerchant, keyId));

        verify(apiKeyRepository, never()).save(any());
    }

    @Test
    void rotateRetainsPreviousSecretAndReturnsNewSecret() {
        UUID merchantId = UUID.randomUUID();
        UUID keyId = UUID.randomUUID();
        ApiKey key = ApiKey.builder()
                .merchant(Merchant.builder().id(merchantId).build())
                .keySecretHash("old-secret")
                .enabled(true)
                .build();
        ApiKeyCreateResponse response = ApiKeyCreateResponse.builder().build();
        when(apiKeyRepository.findById(keyId)).thenReturn(Optional.of(key));
        when(apiKeyRepository.save(key)).thenReturn(key);
        when(apiKeyMapper.toCreateResponse(eq(key), anyString())).thenReturn(response);

        assertSame(response, apiKeyService.rotate(merchantId, keyId));

        assertNotNull(key.getRotatedAt());
        assertNotNull(key.getGracePeriodExpiresAt());
        assertNotNull(key.getPreviousKeySecretHash());
        assertFalse(key.getKeySecretHash().equals("old-secret"));
        verify(apiKeyMapper).toCreateResponse(key, key.getKeySecretHash());
    }

    @Test
    void rotateRejectsDisabledKey() {
        UUID merchantId = UUID.randomUUID();
        UUID keyId = UUID.randomUUID();
        ApiKey key = ApiKey.builder()
                .merchant(Merchant.builder().id(merchantId).build())
                .enabled(false)
                .build();
        when(apiKeyRepository.findById(keyId)).thenReturn(Optional.of(key));

        assertThrows(ResourceNotFoundException.class, () -> apiKeyService.rotate(merchantId, keyId));

        verify(apiKeyRepository, never()).save(any());
    }
}
