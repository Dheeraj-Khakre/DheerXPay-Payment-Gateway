package com.dheerx.DheerX_Pay.merchant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MerchantAddressDto {

    @Size(max = 255, message = "Address line 1 must not exceed 255 characters")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
    private String addressLine2;

    @NotBlank(message = "Landmark is required")
    @Size(max = 150, message = "Landmark must not exceed 150 characters")
    private String landmark;

    @NotBlank(message = "City is required")
    @Size(max = 80, message = "City must not exceed 80 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 80, message = "State must not exceed 80 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 80, message = "Country must not exceed 80 characters")
    private String country;

    @Pattern(regexp = "^[0-9]{4,10}$", message = "Postal code must be numeric and between 4 and 10 digits")
    private String postalCode;
}
