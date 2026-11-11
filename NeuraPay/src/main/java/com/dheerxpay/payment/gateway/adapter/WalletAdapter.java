package com.dheerxpay.payment.gateway.adapter;

import com.dheerxpay.payment.gateway.PaymentAdapter;
import com.dheerxpay.payment.gateway.dto.PaymentRequest;

public class WalletAdapter implements PaymentAdapter {
    @Override
    public void initiatePayment(PaymentRequest paymentRequest) {
        // Implement wallet payment initiation logic here
    }
}
