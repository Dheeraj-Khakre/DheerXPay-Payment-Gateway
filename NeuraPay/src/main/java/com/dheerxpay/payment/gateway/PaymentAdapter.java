package com.dheerxpay.payment.gateway;

import com.dheerxpay.payment.gateway.dto.PaymentRequest;
import com.dheerxpay.payment.gateway.dto.PaymentResult;

public interface PaymentAdapter {

    PaymentResult initiatePayment(PaymentRequest paymentRequest);
}
