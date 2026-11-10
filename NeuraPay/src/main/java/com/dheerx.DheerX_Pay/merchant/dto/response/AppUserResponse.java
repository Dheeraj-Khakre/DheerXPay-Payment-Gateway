package com.dheerx.DheerX_Pay.merchant.dto.response;

import com.dheerx.DheerX_Pay.common.enums.UserRoles;

import java.util.UUID;

/**
 * Response payload returned for a merchant app user.
 */
public record AppUserResponse(
        UUID id,
        String username,
        String email,
        UserRoles role,
        UUID merchantId
) {
}

