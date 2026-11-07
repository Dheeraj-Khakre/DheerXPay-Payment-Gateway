package com.dheerx.DheerX_Pay.merchant.dto;

import java.util.UUID;

/**
 * Response payload returned for a customer record.
 */
public record CustomerResponse(
        UUID id,
        String username,
        String email,
        String phone,
        String gstin,
        UUID merchantId
) {
}

