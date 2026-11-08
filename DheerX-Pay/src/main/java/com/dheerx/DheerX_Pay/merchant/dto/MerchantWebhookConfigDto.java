package com.dheerx.DheerX_Pay.merchant.dto;

import com.dheerx.DheerX_Pay.common.dto.AuditableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MerchantWebhookConfigDto extends AuditableDto {

    private UUID id;

    @NotNull(message = "Merchant is required")
    private UUID merchantId;

    @NotBlank(message = "Webhook target URL is required")
    @URL(message = "Webhook target URL must be valid")
    @Pattern(regexp = "^https?://.+$", message = "Webhook target URL must use HTTP or HTTPS")
    @Size(max = 2048, message = "Webhook target URL must not exceed 2048 characters")
    private String targetUrl;

    @Size(max = 1000, message = "Event type filter must not exceed 1000 characters")
    private String eventTypeFilter;

    private boolean enabled = true;

    @Size(max = 100, message = "Event type must not exceed 100 characters")
    private String eventType;

    @NotBlank(message = "Webhook secret hash is required")
    @Size(max = 255, message = "Webhook secret hash must not exceed 255 characters")
    private String webhookSecretHash;
}
