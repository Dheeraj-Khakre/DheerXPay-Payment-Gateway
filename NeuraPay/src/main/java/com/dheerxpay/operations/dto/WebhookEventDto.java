package com.dheerxpay.operations.dto;

import com.dheerxpay.common.dto.AuditableDto;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class WebhookEventDto extends AuditableDto {

    private UUID id;

    @NotNull(message = "Merchant is required")
    private UUID merchantId;

    @NotBlank(message = "Event type is required")
    @Size(max = 100, message = "Event type must not exceed 100 characters")
    private String eventType;

    @NotNull(message = "Webhook payload is required")
    private JsonNode payload;

    @NotBlank(message = "Webhook target URL is required")
    @URL(message = "Webhook target URL must be valid")
    @Pattern(regexp = "^https?://.+$", message = "Webhook target URL must use HTTP or HTTPS")
    @Size(max = 2048, message = "Webhook target URL must not exceed 2048 characters")
    private String targetUrl;

    @NotBlank(message = "Webhook status is required")
    @Size(max = 30, message = "Webhook status must not exceed 30 characters")
    private String status;

    @Min(value = 0, message = "Attempts cannot be negative")
    private int attempts;

    @Min(value = 100, message = "HTTP response code must be at least 100")
    @Max(value = 599, message = "HTTP response code cannot exceed 599")
    private Integer lastResponseCode;

    private Instant nextRetryAt;
    private Instant lastRetryAt;
    private Instant deliveredAt;
}
