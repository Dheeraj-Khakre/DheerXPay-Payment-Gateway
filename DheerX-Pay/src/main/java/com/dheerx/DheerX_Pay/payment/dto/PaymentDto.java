package com.dheerx.DheerX_Pay.payment.dto;

import com.dheerx.DheerX_Pay.common.dto.AuditableDto;
import com.dheerx.DheerX_Pay.common.dto.MoneyDto;
import com.dheerx.DheerX_Pay.common.enums.PaymentMethod;
import com.dheerx.DheerX_Pay.common.enums.PaymentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class PaymentDto extends AuditableDto {

    private UUID id;

    @NotNull(message = "Order is required")
    private UUID orderId;

    @NotNull(message = "Merchant is required")
    private UUID merchantId;

    @NotBlank(message = "Idempotency key is required")
    @Size(max = 255, message = "Idempotency key must not exceed 255 characters")
    private String idempotencyKey;

    @Valid
    @NotNull(message = "Payment amount is required")
    private MoneyDto amount;

    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    @NotNull(message = "Payment method is required")
    private PaymentMethod method;

    private Map<String, Object> methodDetails;

    @Size(max = 150, message = "Bank reference must not exceed 150 characters")
    private String bankReference;

    @Size(max = 100, message = "Error code must not exceed 100 characters")
    private String errorCode;

    @Size(max = 1000, message = "Error description must not exceed 1000 characters")
    private String errorDescription;

    @Min(value = 0, message = "Refunded amount cannot be negative")
    private long refundedAmountPaise;

    private Instant authorizedAt;
    private Instant failedAt;
    private Instant capturedAt;
    private Instant settledAt;
}
