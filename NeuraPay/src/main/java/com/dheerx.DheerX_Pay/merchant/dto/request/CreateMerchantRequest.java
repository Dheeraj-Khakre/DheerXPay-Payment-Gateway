package com.dheerx.DheerX_Pay.merchant.dto.request;

import com.dheerx.DheerX_Pay.common.enums.BusinessType;
import com.dheerx.DheerX_Pay.merchant.dto.response.MerchantAddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request payload used to register a merchant.
 */
public record CreateMerchantRequest(
        @NotBlank(message = "Merchant code is required")
        @Size(min = 3, max = 30, message = "Merchant code must be between 3 and 30 characters")
        @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Merchant code can contain only letters, numbers, underscore and hyphen")
        String merchantCode,

        @NotBlank(message = "Business name is required")
        @Size(min = 2, max = 150, message = "Business name must be between 2 and 150 characters")
        String businessName,

        @NotNull(message = "Business type is required")
        BusinessType businessType,

        @NotBlank(message = "Legal name is required")
        @Size(max = 150, message = "Legal name must not exceed 150 characters")
        String legalName,

        @NotBlank(message = "Primary contact email is required")
        @Email(message = "Email must be a valid format")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[+]?[0-9()\\-\\s]{8,20}$", message = "Phone number is invalid")
        String phone,

        @Size(max = 255, message = "Website URL must not exceed 255 characters")
        String websiteUrl,

        @Pattern(regexp = "^[A-Z0-9]{15}$", message = "GSTIN must be 15 characters and alphanumeric uppercase")
        String gstId,

        @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "PAN must be a valid Indian PAN format")
        String pan,

        @NotNull(message = "Address is required")
        @Valid
        MerchantAddressDto address
) {
}
