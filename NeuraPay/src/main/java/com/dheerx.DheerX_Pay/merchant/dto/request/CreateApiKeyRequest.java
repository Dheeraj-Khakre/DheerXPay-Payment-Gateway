package com.dheerx.DheerX_Pay.merchant.dto.request;

import com.dheerx.DheerX_Pay.common.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}
