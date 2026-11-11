package com.dheerxpay.gateway.dto;

import com.dheerxpay.common.entity.Money;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest(
        UUID merchantId,
        UUID paymentId,
        UUID orderId,
        String paymentMethod,
        Money amount,
        Map<String, Object> methodDetails
) {
}
