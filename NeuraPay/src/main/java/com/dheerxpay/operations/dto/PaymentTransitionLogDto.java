package com.dheerxpay.operations.dto;

import com.dheerxpay.common.dto.AuditableDto;
import com.dheerxpay.common.enums.PaymentEvent;
import com.dheerxpay.common.enums.PaymentStatus;
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
public class PaymentTransitionLogDto extends AuditableDto {

    private UUID id;

    @NotNull(message = "Payment is required")
    private UUID paymentId;

    private PaymentStatus fromStatus;

    @NotNull(message = "New status is required")
    private PaymentStatus toStatus;

    @NotNull(message = "Event type is required")
    private PaymentEvent event;

    @Size(max = 150, message = "Actor must not exceed 150 characters")
    private String actor;

    @Size(max = 1000, message = "Reason must not exceed 1000 characters")
    private String reason;

    private Instant occurredAt;
}
