package com.dheerxpay.vault.dto;

import com.dheerxpay.common.dto.AuditableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CardTokenDto extends AuditableDto {

    private UUID id;

    @NotBlank(message = "Card token is required")
    @Size(max = 255, message = "Card token must not exceed 255 characters")
    private String token;

    @NotNull(message = "Vault card is required")
    private UUID vaultCardId;

    @NotNull(message = "Customer is required")
    private UUID customerId;

    @NotNull(message = "Merchant is required")
    private UUID merchantId;
}
