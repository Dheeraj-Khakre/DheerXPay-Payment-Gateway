package com.dheerxpay.payment.gateway;

import com.dheerxpay.common.enums.PaymentMethod;
import com.dheerxpay.common.exceptions.ResourceNotFoundException;
import com.dheerxpay.payment.gateway.dto.PaymentRequest;
import com.dheerxpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor

public class PaymentGatewayRouter {

     private final Map<PaymentMethod , PaymentAdapter> paymentAdapter;

    public PaymentResult initiatePayment(PaymentRequest paymentRequest) {

        PaymentAdapter adapter = paymentAdapter.get(paymentRequest.paymentMethod());
        if(adapter == null){
            throw new ResourceNotFoundException("PAYMENT_ADAPTER_NOT_FOUND", "No payment adapter found for method: " + paymentRequest.paymentMethod());
        }
        return adapter.initiatePayment(paymentRequest);
    }
}
