package com.dheerxpay.vault.dto;

import com.dheerxpay.common.dto.AuditableDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class VaultCardDto extends AuditableDto {

    private UUID id;

    @NotNull(message = "Encrypted PAN is required")
    private byte[] encryptedPan;

    @NotNull(message = "Encrypted data encryption key is required")
    private byte[] encryptedDek;

    @NotNull(message = "Last four card digits are required")
    @Pattern(regexp = "^[0-9]{4}$", message = "Last four must contain exactly four digits")
    private String lastFour;

    @Size(max = 30, message = "Card brand must not exceed 30 characters")
    private String brand;

    @Size(min = 6, max = 8, message = "BIN must contain between 6 and 8 digits")
    @Pattern(regexp = "^[0-9]+$", message = "BIN must contain only digits")
    private String bin;

    @NotNull(message = "Expiry month is required")
    @Min(value = 1, message = "Expiry month must be between 1 and 12")
    @Max(value = 12, message = "Expiry month must be between 1 and 12")
    private Integer expiryMonth;

    @NotNull(message = "Expiry year is required")
    @Min(value = 2000, message = "Expiry year must be a four-digit year")
    @Max(value = 9999, message = "Expiry year must be a four-digit year")
    private Integer expiryYear;
}
