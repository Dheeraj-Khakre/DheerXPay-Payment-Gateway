package com.dheerxpay.operations.dto;

import com.dheerxpay.common.dto.AuditableDto;
import jakarta.validation.constraints.Min;
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
public class SettlementDto extends AuditableDto {

    private UUID id;

    @NotNull(message = "Merchant is required")
    private UUID merchantId;

    @Min(value = 0, message = "Gross amount cannot be negative")
    private long grossAmountPaise;

    @Min(value = 0, message = "Refund amount cannot be negative")
    private long refundAmountPaise;

    @Min(value = 0, message = "Fee amount cannot be negative")
    private long feeAmountPaise;

    @Min(value = 0, message = "GST amount cannot be negative")
    private long gstAmountPaise;

    @Min(value = 0, message = "Net amount cannot be negative")
    private long netAmountPaise;

    @NotBlank(message = "Settlement status is required")
    @Size(max = 30, message = "Settlement status must not exceed 30 characters")
    private String status;

    @Size(max = 150, message = "Bank reference must not exceed 150 characters")
    private String bankReference;

    private Instant processedAt;
}
