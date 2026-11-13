package com.dheerxpay.merchant.dto.request;

import com.dheerxpay.common.enums.Environment;
import jakarta.validation.constraints.NotNull;

public record CreateApiKeyRequest(
        @NotNull(message = "Environment is required")
        Environment environment
) {
}
