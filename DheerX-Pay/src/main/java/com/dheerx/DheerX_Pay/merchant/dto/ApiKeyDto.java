package com.dheerx.DheerX_Pay.merchant.dto;

import com.dheerx.DheerX_Pay.common.dto.AuditableDto;
import com.dheerx.DheerX_Pay.common.enums.Environment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ApiKeyDto extends AuditableDto {

    private UUID id;

    @NotNull(message = "Merchant is required")
    private UUID merchantId;

    @NotBlank(message = "Key ID is required")
    @Size(max = 100, message = "Key ID must not exceed 100 characters")
    private String keyId;

    @NotBlank(message = "Key secret hash is required")
    @Size(max = 255, message = "Key secret hash must not exceed 255 characters")
    private String keySecretHash;

    @Size(max = 255, message = "Webhook secret hash must not exceed 255 characters")
    private String webhookSecretHash;

    @NotNull(message = "Environment is required")
    private Environment environment;

    private boolean enabled;
    private Instant lastUsedAt;
    private Instant rotatedAt;
    private Instant gracePeriodExpiresAt;
}
