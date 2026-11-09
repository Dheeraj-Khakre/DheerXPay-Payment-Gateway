package com.dheerx.DheerX_Pay.merchant.controller;

import com.dheerx.DheerX_Pay.common.enums.BusinessType;
import com.dheerx.DheerX_Pay.merchant.dto.request.CreateMerchantRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.MerchantAddressDto;
import com.dheerx.DheerX_Pay.merchant.entities.Merchant;
import com.dheerx.DheerX_Pay.merchant.repository.MerchantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MerchantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MerchantRepository merchantRepository;

    private CreateMerchantRequest request;

    @BeforeEach
    void setUp() {
        MerchantAddressDto address = new MerchantAddressDto();
        address.setAddressLine1("12 Tech Park");
        address.setLandmark("Near City Center");
        address.setCity("Pune");
        address.setState("Maharashtra");
        address.setCountry("India");
        address.setPostalCode("411001");

        MerchantBankDetailsDto bankDetails = new MerchantBankDetailsDto();
        bankDetails.setAccountHolderName("Demo Merchant");
        bankDetails.setBankName("ICICI Bank");
        bankDetails.setAccountNumber("987654321012");
        bankDetails.setIfscCode("ICIC0001234");
        bankDetails.setBranchName("Shivaji Nagar");
        bankDetails.setBeneficiaryType("COMPANY");

        request = new CreateMerchantRequest(
                "INT_101",
                "Integration Merchant",
                BusinessType.PUBLIC_LIMITED,
                "Integration Merchant Pvt Ltd",
                "integration@example.com",
                "+91 9123456789",
                "https://integration.test",
                "27ABCDE1234F1Z5",
                "ABCDE1234F",
                address,
                bankDetails
        );
    }

    @Test
    void createMerchant_shouldReturnCreatedStatus() throws Exception {
        when(merchantRepository.existsByMerchantCode(any())).thenReturn(false);
        when(merchantRepository.existsByEmail(any())).thenReturn(false);
        when(merchantRepository.existsByPhone(any())).thenReturn(false);
        when(merchantRepository.save(any(Merchant.class))).thenAnswer(invocation -> {
            Merchant entity = invocation.getArgument(0);
            entity.setId(java.util.UUID.randomUUID());
            return entity;
        });

        mockMvc.perform(post("/api/v1/merchants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Merchant created successfully"));
    }

    @Test
    void createMerchant_shouldReturnBadRequestForInvalidPayload() throws Exception {
        request = new CreateMerchantRequest(
                "",
                "",
                null,
                "",
                "bad-email",
                "",
                null,
                "INVALID",
                "BAD",
                null,
                null
        );

        mockMvc.perform(post("/api/v1/merchants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
