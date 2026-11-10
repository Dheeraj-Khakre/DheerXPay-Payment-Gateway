package com.dheerx.DheerX_Pay.merchant.dto.response;

import com.dheerx.DheerX_Pay.common.enums.Environment;

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
