package com.dheerx.DheerX_Pay.operations.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SettlementPaymentIdDto {

    @NotNull(message = "Settlement id is required")
    private UUID settlementId;

    @NotNull(message = "Payment id is required")
    private UUID paymentId;
}
