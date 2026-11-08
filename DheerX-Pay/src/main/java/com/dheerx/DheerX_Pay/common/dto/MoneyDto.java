package com.dheerx.DheerX_Pay.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MoneyDto {

    @Positive(message = "Amount must be greater than zero paise")
    private int amountUnit;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO 4217 code")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be an uppercase ISO 4217 code")
    private String currency;
}
