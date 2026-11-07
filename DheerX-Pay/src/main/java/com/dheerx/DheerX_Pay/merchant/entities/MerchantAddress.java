package com.dheerx.DheerX_Pay.merchant.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantAddress {

    @Size(max = 255, message = "Address line 1 must not exceed 255 characters")
    @Column(name = "address_line_1", length = 255)
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
    @Column(name = "address_line_2", length = 255)
    private String addressLine2;

    @NotBlank(message = "Landmark is required")
    @Size(max = 150, message = "Landmark must not exceed 150 characters")
    @Column(name = "landmark", length = 150)
    private String landmark;

    @NotBlank(message = "City is required")
    @Size(max = 80, message = "City must not exceed 80 characters")
    @Column(name = "city", nullable = false, length = 80)
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 80, message = "State must not exceed 80 characters")
    @Column(name = "state", nullable = false, length = 80)
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 80, message = "Country must not exceed 80 characters")
    @Column(name = "country", nullable = false, length = 80)
    private String country;

    @Pattern(regexp = "^[0-9]{4,10}$", message = "Postal code must be numeric and between 4 and 10 digits")
    @Column(name = "postal_code", length = 10)
    private String postalCode;
}

