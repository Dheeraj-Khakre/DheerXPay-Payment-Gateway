package com.dheerx.DheerX_Pay.merchant.controller;

import com.dheerx.DheerX_Pay.common.api.ApiResponse;
import com.dheerx.DheerX_Pay.merchant.dto.request.CreateApiKeyRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.ApiKeyCreateResponse;
import com.dheerx.DheerX_Pay.merchant.dto.response.ApiKeyResponse;
import com.dheerx.DheerX_Pay.merchant.service.ApiKeyService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/merchants/{merchantId}/apikey")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiResponse<ApiKeyCreateResponse>> createApiKey(@PathVariable UUID merchantId, @RequestBody
    @Valid CreateApiKeyRequest apiKeyRequest){
        ApiKeyCreateResponse apiKeyCreateResponse =       apiKeyService.createApiKey(merchantId,apiKeyRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Merchant registered successfully",
                        apiKeyCreateResponse

                ));

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApiKeyResponse>>> getApiKeyListByMerchant(@PathVariable UUID merchantId){
         List<ApiKeyResponse> apiKeyResponseList =       apiKeyService.getApiKeyListByMerchant(merchantId);

        return ResponseEntity
                .ok((ApiResponse.success(
                        "Merchant registered successfully",
                        apiKeyResponseList

                )));

    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<?> revoke(@PathVariable UUID merchantId,@PathVariable UUID keyId){
        apiKeyService.revoke(merchantId,keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public  ResponseEntity<ApiResponse<ApiKeyCreateResponse>> rotate(@PathVariable UUID merchantId,@PathVariable UUID keyId){
        ApiKeyCreateResponse  apiKeyCreateResponse=   apiKeyService.rotate(merchantId,keyId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Merchant registered successfully",
                        apiKeyCreateResponse

                ));
    }




}
