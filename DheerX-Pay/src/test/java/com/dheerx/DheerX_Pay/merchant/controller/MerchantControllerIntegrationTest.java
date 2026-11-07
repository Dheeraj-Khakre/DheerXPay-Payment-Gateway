package com.dheerx.DheerX_Pay.merchant.controller;

import com.dheerx.DheerX_Pay.common.enums.BusinessType;
import com.dheerx.DheerX_Pay.merchant.dto.CreateMerchantRequest;
import com.dheerx.DheerX_Pay.merchant.entities.MerchantAddress;
import com.dheerx.DheerX_Pay.merchant.entities.MerchantBankDetails;
import com.dheerx.DheerX_Pay.merchant.entities.merchant;
import com.dheerx.DheerX_Pay.merchant.repository.MerchantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @MockBean
    private MerchantRepository merchantRepository;

    private CreateMerchantRequest request;

    @BeforeEach
    void setUp() {
        MerchantAddress address = MerchantAddress.builder()
                .addressLine1("12 Tech Park")
                .landmark("Near City Center")
                .city("Pune")
                .state("Maharashtra")
                .country("India")
                .postalCode("411001")
                .build();

        MerchantBankDetails bankDetails = MerchantBankDetails.builder()
                .accountHolderName("Demo Merchant")
                .bankName("ICICI Bank")
                .accountNumber("987654321012")
                .ifscCode("ICIC0001234")
                .branchName("Shivaji Nagar")
                .beneficiaryType("COMPANY")
                .build();

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
        when(merchantRepository.save(any(merchant.class))).thenAnswer(invocation -> {
            merchant entity = invocation.getArgument(0);
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

