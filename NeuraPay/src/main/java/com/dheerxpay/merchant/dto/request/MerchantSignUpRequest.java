package com.dheerxpay.merchant.dto.request;

import com.dheerxpay.common.enums.BusinessType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MerchantSignUpRequest(
        @NotBlank(message = "Business name is required")
        @Size(min = 2, max = 150, message = "Business name must be between 2 and 150 characters")
        String businessName,
        @NotNull(message = "Business type is required")
        BusinessType businessType,

        @NotBlank(message = "Legal name is required")
        @Size(max = 150, message = "Legal name must not exceed 150 characters")
         String name,

         @NotBlank(message = "Primary contact email is required")
         @Email(message = "Email must be a valid format")
         @Size(max = 150, message = "Email must not exceed 150 characters")
         String email,

        @NotBlank(message = "Primary password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        String password,

        @NotBlank(message = "Primary phone is required")
        @Size(min = 10, max = 20, message = "")
        String phone

) {
}
