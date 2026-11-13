package com.dheerxpay.payment.dto.response;

import com.dheerxpay.common.entity.Money;
import com.dheerxpay.common.enums.OrderStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Builder
public record OrderResponse(
        UUID id,
        UUID merchantId,
        OrderStatus status,
        Money amount,
        Integer attempts,
        String receipt,// order id to know merchant
        Map<String,Object> notes,
        Instant expireAt,
        Instant createAt
) {
}
