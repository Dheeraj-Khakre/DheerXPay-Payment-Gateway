package com.dheerx.DheerX_Pay.merchant.service.impl;

import com.dheerx.DheerX_Pay.common.exceptions.ResourceNotFoundException;
import com.dheerx.DheerX_Pay.common.utility.RandomizerUtil;
import com.dheerx.DheerX_Pay.merchant.dto.request.CreateApiKeyRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.ApiKeyCreateResponse;
import com.dheerx.DheerX_Pay.merchant.dto.response.ApiKeyResponse;
import com.dheerx.DheerX_Pay.merchant.entities.ApiKey;
import com.dheerx.DheerX_Pay.merchant.entities.Merchant;
import com.dheerx.DheerX_Pay.merchant.mapper.ApiKeyMapper;
import com.dheerx.DheerX_Pay.merchant.repository.ApiKeyRepository;
import com.dheerx.DheerX_Pay.merchant.repository.MerchantRepository;
import com.dheerx.DheerX_Pay.merchant.service.ApiKeyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl  implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;

    @Override
    @Transactional
    public ApiKeyCreateResponse createApiKey(UUID merchantId, CreateApiKeyRequest apiKeyRequest) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "dxp-"+apiKeyRequest.environment().name().toLowerCase()+"-"+ RandomizerUtil.randomBase64(24);
           String rawSecret = RandomizerUtil.randomBase64(40);// todo: creating secret key

        ApiKey apiKey = apiKeyMapper.toEntity(apiKeyRequest, merchant, keyId, rawSecret);
      apiKey =  apiKeyRepository.save(apiKey);

        return apiKeyMapper.toCreateResponse(apiKey, rawSecret);
    }

    @Override
    public List<ApiKeyResponse> getApiKeyListByMerchant(UUID merchantId) {

        List<ApiKeyResponse> list = apiKeyRepository.findByMerchant_Id(merchantId)
                .stream().map(apiKeyMapper::toResponse)
                .toList();
        return list;

    }

    @Override
    @Transactional
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey apiKey1 = apiKeyRepository
                .findById(keyId)
                .filter(
                        apiKey -> apiKey
                                .getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));
        apiKey1.setEnabled(false);
    }

    @Override
    @Transactional
    public ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository
                .findById(keyId)
                .filter(
                        k -> k
                                .getMerchant().getId().equals(merchantId)
                                && k.isEnabled())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));

        String newRawSecret = RandomizerUtil.randomBase64(40);// todo: creating secret key
            apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());
            apiKey.setRotatedAt(Instant.now());
            apiKey.setKeySecretHash(newRawSecret);// TODO: converte into bcryptPasswordEncoder
            apiKey.setGracePeriodExpiresAt(Instant.now().plusSeconds(86400));


        apiKey =  apiKeyRepository.save(apiKey);

        return apiKeyMapper.toCreateResponse(apiKey, newRawSecret);


    }
}
