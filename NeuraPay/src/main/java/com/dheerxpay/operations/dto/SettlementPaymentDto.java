package com.dheerxpay.operations.dto;

import com.dheerxpay.common.dto.AuditableDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SettlementPaymentDto extends AuditableDto {

    @Valid
    @NotNull(message = "Settlement payment id is required")
    private SettlementPaymentIdDto id;

    @NotNull(message = "Settlement is required")
    private java.util.UUID settlementId;

    @NotNull(message = "Payment is required")
    private java.util.UUID paymentId;
}
