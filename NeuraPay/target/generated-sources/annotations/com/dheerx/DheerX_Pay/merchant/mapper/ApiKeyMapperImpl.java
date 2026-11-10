package com.dheerx.DheerX_Pay.merchant.mapper;

import com.dheerx.DheerX_Pay.common.enums.Environment;
import com.dheerx.DheerX_Pay.merchant.dto.request.CreateApiKeyRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.ApiKeyCreateResponse;
import com.dheerx.DheerX_Pay.merchant.dto.response.ApiKeyResponse;
import com.dheerx.DheerX_Pay.merchant.entities.ApiKey;
import com.dheerx.DheerX_Pay.merchant.entities.Merchant;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-10T22:27:15+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.5.0.2 (Microsoft)"
)
@Component
public class ApiKeyMapperImpl implements ApiKeyMapper {

    @Override
    public ApiKey toEntity(CreateApiKeyRequest request, Merchant merchant, String keyId, String keySecretHash) {
        if ( request == null && merchant == null && keyId == null && keySecretHash == null ) {
            return null;
        }

        ApiKey.ApiKeyBuilder apiKey = ApiKey.builder();

        if ( request != null ) {
            apiKey.environment( request.environment() );
        }
        apiKey.merchant( merchant );
        apiKey.keyId( keyId );
        apiKey.keySecretHash( keySecretHash );

        return apiKey.build();
    }

    @Override
    public ApiKeyCreateResponse toCreateResponse(ApiKey apiKey, String rawSecret) {
        if ( apiKey == null && rawSecret == null ) {
            return null;
        }

        ApiKeyCreateResponse.ApiKeyCreateResponseBuilder apiKeyCreateResponse = ApiKeyCreateResponse.builder();

        if ( apiKey != null ) {
            apiKeyCreateResponse.id( apiKey.getId() );
            apiKeyCreateResponse.keyId( apiKey.getKeyId() );
            apiKeyCreateResponse.environment( apiKey.getEnvironment() );
        }
        apiKeyCreateResponse.keySecretHash( rawSecret );

        return apiKeyCreateResponse.build();
    }

    @Override
    public ApiKeyResponse toResponse(ApiKey apiKey) {
        if ( apiKey == null ) {
            return null;
        }

        boolean enable = false;
        Instant createAt = null;
        UUID id = null;
        String keyId = null;
        Environment environment = null;
        Instant lastUsedAt = null;

        enable = apiKey.isEnabled();
        createAt = apiKey.getCreatedAt();
        id = apiKey.getId();
        keyId = apiKey.getKeyId();
        environment = apiKey.getEnvironment();
        lastUsedAt = apiKey.getLastUsedAt();

        ApiKeyResponse apiKeyResponse = new ApiKeyResponse( id, keyId, environment, enable, createAt, lastUsedAt );

        return apiKeyResponse;
    }
}
