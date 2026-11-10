package com.dheerx.DheerX_Pay.payment.dto.request;

import com.dheerx.DheerX_Pay.common.entity.Money;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record OrderCreateRequest(
        @NotNull(message = "amount is require")
        Money money,
        @Size(max = 100)
        String receipt,// order id to know merchant
        Map<String,Object> notes,
        Instant expireAt
) {
}
