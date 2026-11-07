package com.dheerx.DheerX_Pay.merchant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request body used to create a customer under a merchant.
 */
public record CreateCustomerRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid format")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[+]?[0-9()\\-\\s]{8,20}$", message = "Phone number is invalid")
        String phone,

        @Size(max = 20, message = "GSTIN must not exceed 20 characters")
        String gstin,

        @NotNull(message = "Merchant id is required")
        UUID merchantId
) {
}

