package com.dheerxpay.operations.dto;

import com.dheerxpay.common.dto.AuditableDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DlqEventDto extends AuditableDto {

    private UUID id;

    @NotNull(message = "Webhook event is required")
    private UUID webhookEventId;

    @NotNull(message = "Merchant is required")
    private UUID merchantId;

    @NotBlank(message = "Final error is required")
    @Size(max = 2000, message = "Final error must not exceed 2000 characters")
    private String finalError;

    private Instant movedAt;
    private Instant replayedAt;
}
