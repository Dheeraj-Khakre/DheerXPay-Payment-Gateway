package com.dheerx.DheerX_Pay.merchant.dto;

import com.dheerx.DheerX_Pay.common.dto.AuditableDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class CustomerDto extends AuditableDto {

    private UUID id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid format")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9()\\-\\s]{8,20}$", message = "Phone number is invalid")
    private String phone;

    @Size(max = 20, message = "GSTIN must not exceed 20 characters")
    private String gstin;

    @NotNull(message = "Merchant association is required")
    private UUID merchantId;
}
