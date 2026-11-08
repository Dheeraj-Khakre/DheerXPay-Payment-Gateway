package com.dheerx.DheerX_Pay.merchant.dto;

import com.dheerx.DheerX_Pay.common.enums.BusinessType;
import com.dheerx.DheerX_Pay.common.enums.MerchantStatus;

import java.util.UUID;

/**
 * Response payload returned after merchant creation or retrieval.
 */
public record MerchantResponse(
        UUID id,
        String merchantCode,
        String businessName,
        BusinessType businessType,
        String legalName,
        String email,
        String phone,
        String websiteUrl,
        String gstId,
        String pan,
        MerchantStatus status,
        boolean kycVerified,
        MerchantAddressDto address,
        MerchantBankDetailsDto bankDetails
) {
}
