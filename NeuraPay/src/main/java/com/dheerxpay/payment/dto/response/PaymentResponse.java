package com.dheerxpay.payment.dto.response;

import com.dheerxpay.common.entity.Money;
import com.dheerxpay.common.enums.PaymentMethod;
import com.dheerxpay.common.enums.PaymentStatus;
import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse{

    private UUID id;

    private UUID orderId;

    private UUID merchantId;

    private String idempotencyKey;

    private Money amount;

    private PaymentStatus status;

    private PaymentMethod method;

    private Map<String, Object> methodDetails;

    private String bankReference;

    private String errorCode;

    private String errorDescription;

    private long refundedAmountPaise;

    private Instant authorizedAt;

    private Instant failedAt;

    private Instant capturedAt;

    private Instant settledAt;

    private Instant createdAt;

    private Instant updatedAt;
}
