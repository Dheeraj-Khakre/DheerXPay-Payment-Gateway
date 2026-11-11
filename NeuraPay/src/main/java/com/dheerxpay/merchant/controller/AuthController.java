package com.dheerxpay.merchant.controller;


import com.dheerxpay.common.api.ApiResponse;
import com.dheerxpay.merchant.dto.request.MerchantSignUpRequest;
import com.dheerxpay.merchant.dto.response.MerchantResponse;
import com.dheerxpay.merchant.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                        "Merchant API-KEY successfully",
                        response
                ));
    }
}
