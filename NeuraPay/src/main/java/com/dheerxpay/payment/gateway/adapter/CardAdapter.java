package com.dheerxpay.payment.gateway.adapter;

import com.dheerxpay.payment.gateway.PaymentAdapter;
import com.dheerxpay.payment.gateway.dto.PaymentRequest;

public class CardAdapter implements PaymentAdapter {
    @Override
    public void initiatePayment(PaymentRequest paymentRequest) {
        // Implementation for initiating card payment
    }
}
