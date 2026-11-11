package com.dheerxpay.payment.gateway;

import com.dheerxpay.payment.gateway.dto.PaymentRequest;

public interface PaymentAdapter {

    void initiatePayment(PaymentRequest paymentRequest);
}
