package com.dheerxpay.payment.gateway.adapter;

import com.dheerxpay.payment.gateway.PaymentAdapter;
import com.dheerxpay.payment.gateway.dto.PaymentRequest;
import com.dheerxpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WalletAdapter implements PaymentAdapter {
    @Override
    public PaymentResult initiatePayment(PaymentRequest paymentRequest) {
        // Implement wallet payment initiation logic here
        return null;
    }
}
