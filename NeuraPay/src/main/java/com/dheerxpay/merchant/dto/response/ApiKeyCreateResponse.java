package com.dheerxpay.merchant.dto.response;

import com.dheerxpay.common.enums.Environment;
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
