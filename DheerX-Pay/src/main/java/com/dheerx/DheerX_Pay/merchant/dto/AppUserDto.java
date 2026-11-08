package com.dheerx.DheerX_Pay.merchant.dto;

import com.dheerx.DheerX_Pay.common.dto.AuditableDto;
import com.dheerx.DheerX_Pay.common.enums.UserRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AppUserDto extends AuditableDto {

    private UUID id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid format")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    private String passwordHash;

    @NotNull(message = "User role is required")
    private UserRoles role;

    @NotNull(message = "Merchant is required")
    private UUID merchantId;
}
