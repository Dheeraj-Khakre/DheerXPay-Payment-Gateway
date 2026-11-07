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
public class MerchantBankDetails {

    @NotBlank(message = "Account holder name is required")
    @Size(max = 150, message = "Account holder name must not exceed 150 characters")
    @Column(name = "account_holder_name", nullable = false, length = 150)
    private String accountHolderName;

    @NotBlank(message = "Bank name is required")
    @Size(max = 120, message = "Bank name must not exceed 120 characters")
    @Column(name = "bank_name", nullable = false, length = 120)
    private String bankName;

    @NotBlank(message = "Bank account number is required")
    @Pattern(regexp = "^[0-9]{9,18}$", message = "Bank account number must be 9 to 18 digits")
    @Column(name = "account_number", nullable = false, length = 30)
    private String accountNumber;

    @NotBlank(message = "IFSC code is required")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "IFSC code must be a valid Indian IFSC format")
    @Column(name = "ifsc_code", nullable = false, length = 20)
    private String ifscCode;

    @NotBlank(message = "Branch name is required")
    @Size(max = 120, message = "Branch name must not exceed 120 characters")
    @Column(name = "branch_name", nullable = false, length = 120)
    private String branchName;

    @Size(max = 120, message = "UPI ID must not exceed 120 characters")
    @Column(name = "upi_id", length = 120)
    private String upiId;

    @Size(max = 120, message = "Beneficiary type must not exceed 120 characters")
    @Column(name = "beneficiary_type", length = 120)
    private String beneficiaryType;
}

