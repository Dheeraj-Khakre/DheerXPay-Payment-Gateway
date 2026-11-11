package com.dheerxpay.gateway;

import com.dheerxpay.gateway.dto.PaymentRequest;

public interface PaymentAdapter {

    void initiatePayment(PaymentRequest paymentRequest);
}
