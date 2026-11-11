package com.dheerx.DheerX_Pay.merchant.controller;

import com.dheerx.DheerX_Pay.common.enums.Environment;
import com.dheerx.DheerX_Pay.merchant.dto.request.CreateApiKeyRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.ApiKeyCreateResponse;
import com.dheerx.DheerX_Pay.merchant.dto.response.ApiKeyResponse;
import com.dheerx.DheerX_Pay.merchant.service.ApiKeyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiKeyController.class)
class ApiKeyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ApiKeyService apiKeyService;

    @Test
    void createApiKeyReturnsCreatedResponse() throws Exception {
        UUID merchantId = UUID.randomUUID();
        ApiKeyCreateResponse response = ApiKeyCreateResponse.builder()
                .id(UUID.randomUUID())
                .keyId("sandbox-key")
                .keySecretHash("one-time-secret")
                .environment(Environment.SANDBOX)
                .build();
        when(apiKeyService.createApiKey(eq(merchantId), eq(new CreateApiKeyRequest(Environment.SANDBOX))))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/merchants/{merchantId}/apikey", merchantId)
                        .contentType(APPLICATION_JSON)
                        .content("{\"environment\":\"SANDBOX\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.keyId").value("sandbox-key"))
                .andExpect(jsonPath("$.data.keySecretHash").value("one-time-secret"));

        verify(apiKeyService).createApiKey(merchantId, new CreateApiKeyRequest(Environment.SANDBOX));
    }

    @Test
    void createApiKeyRejectsUnknownEnvironment() throws Exception {
        UUID merchantId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/merchants/{merchantId}/apikey", merchantId)
                        .contentType(APPLICATION_JSON)
                        .content("{\"environment\":\"NOT_AN_ENVIRONMENT\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        verifyNoInteractions(apiKeyService);
    }

    @Test
    void createApiKeyRejectsMissingEnvironment() throws Exception {
        mockMvc.perform(post("/api/v1/merchants/{merchantId}/apikey", UUID.randomUUID())
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.environment").value("Environment is required"));

        verifyNoInteractions(apiKeyService);
    }

    @Test
    void invalidMerchantIdPathParameterReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/merchants/not-a-uuid/apikey"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        verifyNoInteractions(apiKeyService);
    }

    @Test
    void listsApiKeys() throws Exception {
        UUID merchantId = UUID.randomUUID();
        when(apiKeyService.getApiKeyListByMerchant(merchantId)).thenReturn(List.of(
                new ApiKeyResponse(UUID.randomUUID(), "test-key", Environment.TEST, true, null, null)
        ));

        mockMvc.perform(get("/api/v1/merchants/{merchantId}/apikey", merchantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].keyId").value("test-key"))
                .andExpect(jsonPath("$.data[0].enable").value(true));

        verify(apiKeyService).getApiKeyListByMerchant(merchantId);
    }

    @Test
    void revokeReturnsNoContent() throws Exception {
        UUID merchantId = UUID.randomUUID();
        UUID keyId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/merchants/{merchantId}/apikey/{keyId}", merchantId, keyId))
                .andExpect(status().isNoContent());

        verify(apiKeyService).revoke(merchantId, keyId);
    }

    @Test
    void rotateReturnsCreatedResponse() throws Exception {
        UUID merchantId = UUID.randomUUID();
        UUID keyId = UUID.randomUUID();
        ApiKeyCreateResponse response = ApiKeyCreateResponse.builder()
                .id(keyId)
                .keyId("rotated-key")
                .keySecretHash("rotated-secret")
                .environment(Environment.TEST)
                .build();
        when(apiKeyService.rotate(merchantId, keyId)).thenReturn(response);

        mockMvc.perform(post("/api/v1/merchants/{merchantId}/apikey/{keyId}/rotate", merchantId, keyId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.keyId").value("rotated-key"))
                .andExpect(jsonPath("$.data.keySecretHash").value("rotated-secret"));

        verify(apiKeyService).rotate(merchantId, keyId);
    }
}
