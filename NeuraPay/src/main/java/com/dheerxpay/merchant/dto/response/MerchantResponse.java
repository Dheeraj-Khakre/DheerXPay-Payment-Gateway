package com.dheerxpay.merchant.dto.response;

import com.dheerxpay.common.enums.BusinessType;
import com.dheerxpay.common.enums.MerchantStatus;
import lombok.Builder;

import java.util.UUID;

/**
 * Response payload returned after merchant creation or retrieval.
 */
@Builder
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
        MerchantAddressDto address
) {
}
