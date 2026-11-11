package com.dheerxpay.gateway.adapter;

import com.dheerxpay.gateway.PaymentAdapter;
import com.dheerxpay.gateway.dto.PaymentRequest;

public class CardAdapter implements PaymentAdapter {
    @Override
    public void initiatePayment(PaymentRequest paymentRequest) {
        // Implementation for initiating card payment
    }
}
