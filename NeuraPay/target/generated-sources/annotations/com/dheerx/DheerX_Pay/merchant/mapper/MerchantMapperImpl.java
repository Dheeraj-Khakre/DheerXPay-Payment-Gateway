package com.dheerx.DheerX_Pay.merchant.mapper;

import com.dheerx.DheerX_Pay.common.enums.MerchantStatus;
import com.dheerx.DheerX_Pay.common.enums.UserRoles;
import com.dheerx.DheerX_Pay.merchant.dto.request.MerchantSignUpRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.MerchantAddressDto;
import com.dheerx.DheerX_Pay.merchant.dto.response.MerchantResponse;
import com.dheerx.DheerX_Pay.merchant.entities.AppUser;
import com.dheerx.DheerX_Pay.merchant.entities.Merchant;
import com.dheerx.DheerX_Pay.merchant.entities.MerchantAddress;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-10T22:27:16+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.5.0.2 (Microsoft)"
)
@Component
public class MerchantMapperImpl implements MerchantMapper {

    @Override
    public Merchant toEntity(MerchantSignUpRequest request, MerchantStatus status, String merchantCode) {
        if ( request == null && status == null && merchantCode == null ) {
            return null;
        }

        Merchant.MerchantBuilder merchant = Merchant.builder();

        if ( request != null ) {
            merchant.legalName( request.name() );
            merchant.businessName( request.businessName() );
            merchant.businessType( request.businessType() );
            merchant.email( request.email() );
            merchant.phone( request.phone() );
        }
        merchant.status( status );
        merchant.merchantCode( merchantCode );

        return merchant.build();
    }

    @Override
    public AppUser toAppUser(MerchantSignUpRequest request, Merchant merchant, UserRoles role) {
        if ( request == null && merchant == null && role == null ) {
            return null;
        }

        AppUser.AppUserBuilder appUser = AppUser.builder();

        if ( request != null ) {
            appUser.email( request.email() );
            appUser.passwordHash( request.password() );
        }
        appUser.merchant( merchant );
        appUser.role( role );

        return appUser.build();
    }

    @Override
    public MerchantResponse toResponse(Merchant merchant) {
        if ( merchant == null ) {
            return null;
        }

        MerchantResponse.MerchantResponseBuilder merchantResponse = MerchantResponse.builder();

        merchantResponse.id( merchant.getId() );
        merchantResponse.merchantCode( merchant.getMerchantCode() );
        merchantResponse.businessName( merchant.getBusinessName() );
        merchantResponse.businessType( merchant.getBusinessType() );
        merchantResponse.legalName( merchant.getLegalName() );
        merchantResponse.email( merchant.getEmail() );
        merchantResponse.phone( merchant.getPhone() );
        merchantResponse.websiteUrl( merchant.getWebsiteUrl() );
        merchantResponse.gstId( merchant.getGstId() );
        merchantResponse.pan( merchant.getPan() );
        merchantResponse.status( merchant.getStatus() );
        merchantResponse.kycVerified( merchant.isKycVerified() );
        merchantResponse.address( toAddressDto( merchant.getAddress() ) );

        return merchantResponse.build();
    }

    @Override
    public MerchantAddressDto toAddressDto(MerchantAddress address) {
        if ( address == null ) {
            return null;
        }

        MerchantAddressDto merchantAddressDto = new MerchantAddressDto();

        merchantAddressDto.setAddressLine1( address.getAddressLine1() );
        merchantAddressDto.setAddressLine2( address.getAddressLine2() );
        merchantAddressDto.setLandmark( address.getLandmark() );
        merchantAddressDto.setCity( address.getCity() );
        merchantAddressDto.setState( address.getState() );
        merchantAddressDto.setCountry( address.getCountry() );
        merchantAddressDto.setPostalCode( address.getPostalCode() );

        return merchantAddressDto;
    }
}
