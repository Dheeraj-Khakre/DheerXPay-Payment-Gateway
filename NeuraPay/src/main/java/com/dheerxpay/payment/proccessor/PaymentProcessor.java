package com.dheerxpay.payment.proccessor;

import com.dheerxpay.payment.proccessor.dto.PaymentProcessorRequest;
import com.dheerxpay.payment.proccessor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    public PaymentProcessorResponse charge(PaymentProcessorRequest request);
}
