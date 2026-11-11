package com.dheerxpay.merchant.mapper;

import com.dheerxpay.common.enums.MerchantStatus;
import com.dheerxpay.common.enums.UserRoles;
import com.dheerxpay.merchant.dto.request.MerchantSignUpRequest;
import com.dheerxpay.merchant.dto.response.MerchantAddressDto;
import com.dheerxpay.merchant.dto.response.MerchantResponse;
import com.dheerxpay.merchant.entities.AppUser;
import com.dheerxpay.merchant.entities.Merchant;
import com.dheerxpay.merchant.entities.MerchantAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MerchantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "merchantCode", source = "merchantCode")
    @Mapping(target = "legalName", source = "request.name")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "kycVerified", ignore = true)
    @Mapping(target = "websiteUrl", ignore = true)
    @Mapping(target = "gstId", ignore = true)
    @Mapping(target = "pan", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "bankDetails", ignore = true)
    @Mapping(target = "version", ignore = true)
    Merchant toEntity(
            MerchantSignUpRequest request,
            MerchantStatus status,
            String merchantCode
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "passwordHash", source = "request.password")
    @Mapping(target = "merchant", source = "merchant")
    @Mapping(target = "role", source = "role")
    AppUser toAppUser(MerchantSignUpRequest request, Merchant merchant, UserRoles role);

    MerchantResponse toResponse(Merchant merchant);

    MerchantAddressDto toAddressDto(MerchantAddress address);
}
