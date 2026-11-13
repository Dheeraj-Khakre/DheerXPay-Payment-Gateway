package com.dheerxpay.operations.dto;

import com.dheerxpay.common.dto.AuditableDto;
import com.dheerxpay.common.dto.MoneyDto;
import com.dheerxpay.common.enums.RefoundStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RefundDto extends AuditableDto {

    private UUID id;

    @NotNull(message = "Payment is required")
    private UUID paymentId;

    @NotNull(message = "Merchant is required")
    private UUID merchantId;

    @Valid
    @NotNull(message = "Refund amount is required")
    private MoneyDto amount;

    @NotNull(message = "Refund status is required")
    private RefoundStatus status;

    @Size(max = 150, message = "Bank reference must not exceed 150 characters")
    private String bankReference;

    @Size(max = 100, message = "Error code must not exceed 100 characters")
    private String errorCode;

    @Size(max = 1000, message = "Error description must not exceed 1000 characters")
    private Map<String, Object> errorDescription;

    private Instant processedAt;
}
