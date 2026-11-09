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
public class MerchantBankDetailsDto {

    @NotBlank(message = "Account holder name is required")
    @Size(max = 150, message = "Account holder name must not exceed 150 characters")
    private String accountHolderName;

    @NotBlank(message = "Bank name is required")
    @Size(max = 120, message = "Bank name must not exceed 120 characters")
    private String bankName;

    @NotBlank(message = "Bank account number is required")
    @Pattern(regexp = "^[0-9]{9,18}$", message = "Bank account number must be 9 to 18 digits")
    private String accountNumber;

    @NotBlank(message = "IFSC code is required")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "IFSC code must be a valid Indian IFSC format")
    private String ifscCode;

    @NotBlank(message = "Branch name is required")
    @Size(max = 120, message = "Branch name must not exceed 120 characters")
    private String branchName;

    @Size(max = 120, message = "UPI ID must not exceed 120 characters")
    private String upiId;

    @Size(max = 120, message = "Beneficiary type must not exceed 120 characters")
    private String beneficiaryType;
}
