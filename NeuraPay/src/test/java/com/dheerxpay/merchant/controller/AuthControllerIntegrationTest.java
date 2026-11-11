package com.dheerxpay.merchant.controller;

import com.dheerxpay.common.enums.MerchantStatus;
import com.dheerxpay.common.exceptions.DuplicateResourceException;
import com.dheerxpay.merchant.dto.request.MerchantSignUpRequest;
import com.dheerxpay.merchant.dto.response.MerchantResponse;
import com.dheerxpay.merchant.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthService authService;

    @Test
    void signupReturnsCreatedResponse() throws Exception {
        MerchantResponse response = MerchantResponse.builder()
                .id(UUID.randomUUID())
                .businessName("Example Store")
                .email("owner@example.com")
                .status(MerchantStatus.PENDING_KYC)
                .build();
        when(authService.signUp(any(MerchantSignUpRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "businessName": "Example Store",
                                  "businessType": "PRIVATE_LIMITED",
                                  "name": "Example Legal",
                                  "email": "owner@example.com",
                                  "password": "password123",
                                  "phone": "1234567890"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.businessName").value("Example Store"))
                .andExpect(jsonPath("$.data.email").value("owner@example.com"));

        verify(authService).signUp(any(MerchantSignUpRequest.class));
    }

    @Test
    void signupRejectsInvalidRequestBeforeCallingService() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "businessName": "",
                                  "businessType": "PRIVATE_LIMITED",
                                  "name": "Example Legal",
                                  "email": "not-an-email",
                                  "password": "short",
                                  "phone": "1234567890"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.data.businessName").exists())
                .andExpect(jsonPath("$.data.email").exists());

        verifyNoInteractions(authService);
    }

    @Test
    void signupMapsDuplicateEmailToConflict() throws Exception {
        when(authService.signUp(any(MerchantSignUpRequest.class)))
                .thenThrow(new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL", "Email already exists"));

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "businessName": "Example Store",
                                  "businessType": "PRIVATE_LIMITED",
                                  "name": "Example Legal",
                                  "email": "owner@example.com",
                                  "password": "password123",
                                  "phone": "1234567890"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCore").value("DUPLICATE_MERCHANT_EMAIL"));
    }

    @Test
    void signupRejectsMalformedJson() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        verifyNoInteractions(authService);
    }
}
