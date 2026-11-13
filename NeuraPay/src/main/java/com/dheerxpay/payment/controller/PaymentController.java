package com.dheerxpay.payment.controller;

import com.dheerxpay.common.api.ApiResponse;
import com.dheerxpay.payment.dto.request.PaymentInitRequest;
import com.dheerxpay.payment.dto.response.PaymentResponse;
import com.dheerxpay.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor

public class PaymentController {

    private final PaymentService paymentService;
    UUID merchantId = UUID.fromString("f3e1c8d0-4b2a-4c9e-9f5b-1a2b3c4d5e6f"); // Replace with your actual merchant ID

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> initiatePayment(@Valid @RequestBody PaymentInitRequest request) {
        // Call the payment service to initiate the payment
        PaymentResponse paymentResponse = paymentService.initiatePayment(merchantId ,  request);

        // Return the response wrapped in ApiResponse
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Payment initiated successfully",
                paymentResponse));
    }

}

