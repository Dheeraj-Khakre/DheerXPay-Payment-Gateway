package com.dheerx.DheerX_Pay.merchant.controller;


import com.dheerx.DheerX_Pay.common.api.ApiResponse;
import com.dheerx.DheerX_Pay.merchant.dto.request.MerchantSignUpRequest;
import com.dheerx.DheerX_Pay.merchant.dto.response.MerchantResponse;
import com.dheerx.DheerX_Pay.merchant.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MerchantResponse>> signup(
            @RequestBody @Valid MerchantSignUpRequest request) {

        MerchantResponse response = authService.signUp(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Merchant registered successfully",
                        response
                ));
    }
}
