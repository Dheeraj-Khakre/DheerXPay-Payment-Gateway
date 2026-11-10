package com.dheerx.DheerX_Pay.merchant.dto.response;

import com.dheerx.DheerX_Pay.common.enums.Environment;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ApiKeyCreateResponse(
        UUID id,
        String keyId,
        String keySecretHash,
        Environment environment
) {
}
