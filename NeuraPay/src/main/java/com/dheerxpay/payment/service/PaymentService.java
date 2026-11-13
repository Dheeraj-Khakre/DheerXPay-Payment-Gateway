package com.dheerxpay.payment.service;


import com.dheerxpay.payment.dto.request.PaymentInitRequest;
import com.dheerxpay.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {
    PaymentResponse initiatePayment(UUID merchantId, PaymentInitRequest request);
}
