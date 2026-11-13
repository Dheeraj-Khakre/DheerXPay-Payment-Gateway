package com.dheerxpay.merchant.mapper;

import com.dheerxpay.merchant.dto.request.CreateApiKeyRequest;
import com.dheerxpay.merchant.dto.response.ApiKeyCreateResponse;
import com.dheerxpay.merchant.dto.response.ApiKeyResponse;
import com.dheerxpay.merchant.entities.ApiKey;
import com.dheerxpay.merchant.entities.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApiKeyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "merchant", source = "merchant")
    @Mapping(target = "keyId", source = "keyId")
    @Mapping(target = "keySecretHash", source = "keySecretHash")
    @Mapping(target = "previousKeySecretHash", ignore = true)
    @Mapping(target = "webhookSecretHash", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "lastUsedAt", ignore = true)
    @Mapping(target = "rotatedAt", ignore = true)
    @Mapping(target = "gracePeriodExpiresAt", ignore = true)
    ApiKey toEntity(
            CreateApiKeyRequest request,
            Merchant merchant,
            String keyId,
            String keySecretHash
    );

    @Mapping(target = "keySecretHash", source = "rawSecret")
    ApiKeyCreateResponse toCreateResponse(ApiKey apiKey, String rawSecret);

    @Mapping(target = "enable", source = "enabled")
    @Mapping(target = "createAt", source = "createdAt")
    ApiKeyResponse toResponse(ApiKey apiKey);
}
