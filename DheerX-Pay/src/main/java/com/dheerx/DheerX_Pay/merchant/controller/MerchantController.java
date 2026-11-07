package com.dheerx.DheerX_Pay.merchant.controller;

import com.dheerx.DheerX_Pay.common.api.ApiResponse;
import com.dheerx.DheerX_Pay.common.enums.MerchantStatus;
import com.dheerx.DheerX_Pay.merchant.dto.CreateMerchantRequest;
import com.dheerx.DheerX_Pay.merchant.dto.MerchantResponse;
import com.dheerx.DheerX_Pay.merchant.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping
    public ResponseEntity<ApiResponse<MerchantResponse>> createMerchant(
            @Valid @RequestBody CreateMerchantRequest request) {
        ApiResponse<MerchantResponse> response = merchantService.createMerchant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MerchantResponse>>> getAllMerchants() {
        return ResponseEntity.ok(merchantService.getAllMerchants());
    }

    @GetMapping("/{merchantId}")
    public ResponseEntity<ApiResponse<MerchantResponse>> getMerchantById(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(merchantService.getMerchantById(merchantId));
    }

    @GetMapping("/code/{merchantCode}")
    public ResponseEntity<ApiResponse<MerchantResponse>> getMerchantByCode(@PathVariable String merchantCode) {
        return ResponseEntity.ok(merchantService.getMerchantByCode(merchantCode));
    }

    @PatchMapping("/{merchantId}/status")
    public ResponseEntity<ApiResponse<MerchantResponse>> updateMerchantStatus(
            @PathVariable UUID merchantId,
            @RequestParam MerchantStatus status) {
        return ResponseEntity.ok(merchantService.updateMerchantStatus(merchantId, status));
    }
}

