package com.dheerx.DheerX_Pay.payment.dto;

import com.dheerx.DheerX_Pay.common.dto.AuditableDto;
import com.dheerx.DheerX_Pay.common.dto.MoneyDto;
import com.dheerx.DheerX_Pay.common.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
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
public class OrderRecordDto extends AuditableDto {

    private UUID id;

    @NotNull(message = "Merchant is required")
    private UUID merchantId;

    @NotBlank(message = "Idempotency key is required")
    @Size(max = 255, message = "Idempotency key must not exceed 255 characters")
    private String idempotencyKey;

    @Valid
    @NotNull(message = "Order amount is required")
    private MoneyDto amount;

    @NotNull(message = "Order status is required")
    private OrderStatus status;

    @Min(value = 0, message = "Attempts cannot be negative")
    private Integer attempts;

    private Map<String, Object> notes;
    private Instant expiresAt;
}
