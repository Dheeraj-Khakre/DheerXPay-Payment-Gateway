package com.dheerx.DheerX_Pay.payment.dto.response;

import com.dheerx.DheerX_Pay.common.entity.Money;
import com.dheerx.DheerX_Pay.common.enums.OrderStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Builder
public record CreateOrderResponse(
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
