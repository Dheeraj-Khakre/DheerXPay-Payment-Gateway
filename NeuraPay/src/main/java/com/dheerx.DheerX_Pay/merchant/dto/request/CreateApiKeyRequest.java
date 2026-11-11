package com.dheerx.DheerX_Pay.merchant.dto.request;

import com.dheerx.DheerX_Pay.common.enums.Environment;
import jakarta.validation.constraints.NotNull;

public record CreateApiKeyRequest(
        @NotNull(message = "Environment is required")
        Environment environment
) {
}
