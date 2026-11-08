package com.dheerx.DheerX_Pay.merchant.service;

import com.dheerx.DheerX_Pay.common.api.ApiResponse;
import com.dheerx.DheerX_Pay.common.enums.MerchantStatus;
import com.dheerx.DheerX_Pay.common.exceptions.DuplicateResourceException;
import com.dheerx.DheerX_Pay.common.exceptions.ResourceNotFoundException;
import com.dheerx.DheerX_Pay.merchant.dto.CreateMerchantRequest;
import com.dheerx.DheerX_Pay.merchant.dto.MerchantAddressDto;
import com.dheerx.DheerX_Pay.merchant.dto.MerchantBankDetailsDto;
import com.dheerx.DheerX_Pay.merchant.dto.MerchantResponse;
import com.dheerx.DheerX_Pay.merchant.entities.Merchant;
import com.dheerx.DheerX_Pay.merchant.entities.MerchantAddress;
import com.dheerx.DheerX_Pay.merchant.entities.MerchantBankDetails;
import com.dheerx.DheerX_Pay.merchant.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    @Transactional
    public ApiResponse<MerchantResponse> createMerchant(CreateMerchantRequest request) {
        validateDuplicateBusinessIdentifiers(request);

        Merchant merchantEntity = Merchant.builder()
                .merchantCode(request.merchantCode())
                .businessName(request.businessName())
                .businessType(request.businessType())
                .legalName(request.legalName())
                .email(request.email())
                .phone(request.phone())
                .websiteUrl(request.websiteUrl())
                .gstId(request.gstId())
                .pan(request.pan())
                .status(MerchantStatus.PENDING_KYC)
                .kycVerified(false)
                .address(toEntity(request.address()))
                .bankDetails(toEntity(request.bankDetails()))
                .build();

        Merchant saved = merchantRepository.save(merchantEntity);
        MerchantResponse response = mapToResponse(saved);
        return new ApiResponse<>("SUCCESS", "Merchant created successfully", response);
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<MerchantResponse>> getAllMerchants() {
        List<MerchantResponse> merchants = merchantRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new ApiResponse<>("SUCCESS", "Merchants retrieved successfully", merchants);
    }

    @Transactional(readOnly = true)
    public ApiResponse<MerchantResponse> getMerchantById(UUID merchantId) {
        Merchant merchantEntity = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found with id: " + merchantId));

        return new ApiResponse<>("SUCCESS", "Merchant retrieved successfully", mapToResponse(merchantEntity));
    }

    @Transactional(readOnly = true)
    public ApiResponse<MerchantResponse> getMerchantByCode(String merchantCode) {
        Merchant merchantEntity = merchantRepository.findByMerchantCode(merchantCode)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found with code: " + merchantCode));

        return new ApiResponse<>("SUCCESS", "Merchant retrieved successfully", mapToResponse(merchantEntity));
    }

    @Transactional
    public ApiResponse<MerchantResponse> updateMerchantStatus(UUID merchantId, MerchantStatus status) {
        Merchant merchantEntity = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found with id: " + merchantId));

        merchantEntity.setStatus(status);
        merchantEntity.setKycVerified(status == MerchantStatus.ACTIVE);

        Merchant saved = merchantRepository.save(merchantEntity);
        return new ApiResponse<>("SUCCESS", "Merchant status updated successfully", mapToResponse(saved));
    }

    private void validateDuplicateBusinessIdentifiers(CreateMerchantRequest request) {
        if (merchantRepository.existsByMerchantCode(request.merchantCode())) {
            throw new DuplicateResourceException("Merchant code already exists: " + request.merchantCode());
        }

        if (merchantRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Merchant email already exists: " + request.email());
        }

        if (merchantRepository.existsByPhone(request.phone())) {
            throw new DuplicateResourceException("Merchant phone already exists: " + request.phone());
        }
    }

    private MerchantResponse mapToResponse(Merchant merchantEntity) {
        return new MerchantResponse(
                merchantEntity.getId(),
                merchantEntity.getMerchantCode(),
                merchantEntity.getBusinessName(),
                merchantEntity.getBusinessType(),
                merchantEntity.getLegalName(),
                merchantEntity.getEmail(),
                merchantEntity.getPhone(),
                merchantEntity.getWebsiteUrl(),
                merchantEntity.getGstId(),
                merchantEntity.getPan(),
                merchantEntity.getStatus(),
                merchantEntity.isKycVerified(),
                toDto(merchantEntity.getAddress()),
                toDto(merchantEntity.getBankDetails()));
    }

    private MerchantAddress toEntity(MerchantAddressDto address) {
        return MerchantAddress.builder()
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }

    private MerchantBankDetails toEntity(MerchantBankDetailsDto bankDetails) {
        return MerchantBankDetails.builder()
                .accountHolderName(bankDetails.getAccountHolderName())
                .bankName(bankDetails.getBankName())
                .accountNumber(bankDetails.getAccountNumber())
                .ifscCode(bankDetails.getIfscCode())
                .branchName(bankDetails.getBranchName())
                .upiId(bankDetails.getUpiId())
                .beneficiaryType(bankDetails.getBeneficiaryType())
                .build();
    }

    private MerchantAddressDto toDto(MerchantAddress address) {
        if (address == null) {
            return null;
        }

        MerchantAddressDto dto = new MerchantAddressDto();
        dto.setAddressLine1(address.getAddressLine1());
        dto.setAddressLine2(address.getAddressLine2());
        dto.setLandmark(address.getLandmark());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setPostalCode(address.getPostalCode());
        return dto;
    }

    private MerchantBankDetailsDto toDto(MerchantBankDetails bankDetails) {
        if (bankDetails == null) {
            return null;
        }

        MerchantBankDetailsDto dto = new MerchantBankDetailsDto();
        dto.setAccountHolderName(bankDetails.getAccountHolderName());
        dto.setBankName(bankDetails.getBankName());
        dto.setAccountNumber(bankDetails.getAccountNumber());
        dto.setIfscCode(bankDetails.getIfscCode());
        dto.setBranchName(bankDetails.getBranchName());
        dto.setUpiId(bankDetails.getUpiId());
        dto.setBeneficiaryType(bankDetails.getBeneficiaryType());
        return dto;
    }
}
