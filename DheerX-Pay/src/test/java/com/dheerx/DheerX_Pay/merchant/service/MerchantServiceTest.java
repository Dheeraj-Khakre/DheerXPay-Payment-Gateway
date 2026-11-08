package com.dheerx.DheerX_Pay.merchant.service;

import com.dheerx.DheerX_Pay.common.api.ApiResponse;
import com.dheerx.DheerX_Pay.common.enums.BusinessType;
import com.dheerx.DheerX_Pay.common.enums.MerchantStatus;
import com.dheerx.DheerX_Pay.common.exceptions.DuplicateResourceException;
import com.dheerx.DheerX_Pay.common.exceptions.ResourceNotFoundException;
import com.dheerx.DheerX_Pay.merchant.dto.CreateMerchantRequest;
import com.dheerx.DheerX_Pay.merchant.dto.MerchantAddressDto;
import com.dheerx.DheerX_Pay.merchant.dto.MerchantBankDetailsDto;
import com.dheerx.DheerX_Pay.merchant.dto.MerchantResponse;
import com.dheerx.DheerX_Pay.merchant.entities.Merchant;
import com.dheerx.DheerX_Pay.merchant.repository.MerchantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private MerchantService merchantService;

    private CreateMerchantRequest request;

    @BeforeEach
    void setUp() {
        MerchantAddressDto address = new MerchantAddressDto();
        address.setAddressLine1("14 Main Street");
        address.setLandmark("Near Metro Station");
        address.setCity("Bengaluru");
        address.setState("Karnataka");
        address.setCountry("India");
        address.setPostalCode("560001");

        MerchantBankDetailsDto bankDetails = new MerchantBankDetailsDto();
        bankDetails.setAccountHolderName("DheerX Pay Pvt Ltd");
        bankDetails.setBankName("HDFC Bank");
        bankDetails.setAccountNumber("123456789012");
        bankDetails.setIfscCode("HDFC0001234");
        bankDetails.setBranchName("Koramangala");
        bankDetails.setBeneficiaryType("COMPANY");

        request = new CreateMerchantRequest(
                "DHX_001",
                "DheerX Pay",
                BusinessType.PRIVATE_LIMITED,
                "DheerX Pay Private Limited",
                "merchant@example.com",
                "+91 9876543210",
                "https://dheerxpay.com",
                "29ABCDE1234F1Z5",
                "ABCDE1234F",
                address,
                bankDetails
        );
    }

    @Test
    void createMerchant_shouldCreateSuccessfully() {
        when(merchantRepository.existsByMerchantCode(anyString())).thenReturn(false);
        when(merchantRepository.existsByEmail(anyString())).thenReturn(false);
        when(merchantRepository.existsByPhone(anyString())).thenReturn(false);

        when(merchantRepository.save(any(Merchant.class))).thenAnswer(invocation -> {
            Merchant merchant = invocation.getArgument(0);
            merchant.setId(UUID.randomUUID());
            return merchant;
        });

        ApiResponse<MerchantResponse> response = merchantService.createMerchant(request);

        assertNotNull(response);
        assertEquals("SUCCESS", response.status());
        assertEquals("DheerX Pay", response.data().businessName());
        verify(merchantRepository).save(any(Merchant.class));
    }

    @Test
    void createMerchant_shouldThrowDuplicateWhenCodeExists() {
        when(merchantRepository.existsByMerchantCode("DHX_001")).thenReturn(true);

        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class,
                () -> merchantService.createMerchant(request));

        assertTrue(ex.getMessage().contains("Merchant code already exists"));
    }

    @Test
    void getMerchantById_shouldReturnMerchant() {
        UUID merchantId = UUID.randomUUID();
        Merchant entity = Merchant.builder()
                .id(merchantId)
                .merchantCode("DHX_002")
                .businessName("Test Merchant")
                .businessType(BusinessType.SOLE_PROPRIETORSHIP)
                .legalName("Test Merchant")
                .email("test@example.com")
                .phone("+91 9999999999")
                .status(MerchantStatus.ACTIVE)
                .build();

        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(entity));

        ApiResponse<MerchantResponse> response = merchantService.getMerchantById(merchantId);

        assertEquals("SUCCESS", response.status());
        assertEquals("DHX_002", response.data().merchantCode());
    }

    @Test
    void getMerchantById_shouldThrowWhenNotFound() {
        UUID merchantId = UUID.randomUUID();
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> merchantService.getMerchantById(merchantId));

        assertTrue(ex.getMessage().contains("Merchant not found"));
    }

    @Test
    void getAllMerchants_shouldReturnList() {
        Merchant entity = Merchant.builder()
                .id(UUID.randomUUID())
                .merchantCode("DHX_003")
                .businessName("Another Merchant")
                .businessType(BusinessType.LLP)
                .legalName("Another Merchant")
                .email("another@example.com")
                .phone("+91 8888888888")
                .status(MerchantStatus.PENDING_KYC)
                .build();

        when(merchantRepository.findAll()).thenReturn(List.of(entity));

        ApiResponse<List<MerchantResponse>> response = merchantService.getAllMerchants();

        assertEquals("SUCCESS", response.status());
        assertEquals(1, response.data().size());
    }
}
