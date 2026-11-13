package com.dheerxpay.merchant.dto.response;

import com.dheerxpay.common.enums.Environment;

import java.time.Instant;
import java.util.UUID;

public record ApiKeyResponse(
        UUID id,
        String keyId,
        Environment environment,
        boolean enable,
        Instant createAt,
        Instant lastUsedAt
) {
}
