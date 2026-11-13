package com.dheerxpay.payment.gateway.dto;

import com.dheerxpay.common.entity.Money;
import com.dheerxpay.common.enums.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Builder
public record PaymentRequest(
        UUID merchantId,
        UUID paymentId,
        UUID orderId,
        PaymentMethod paymentMethod,
        Money amount,
        Map<String, Object> methodDetails
) {
}
