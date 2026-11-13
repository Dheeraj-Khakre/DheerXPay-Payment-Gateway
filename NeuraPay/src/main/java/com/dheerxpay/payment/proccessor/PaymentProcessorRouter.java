package com.dheerxpay.payment.proccessor;

import com.dheerxpay.common.enums.PaymentMethod;
import com.dheerxpay.payment.proccessor.dto.PaymentProcessorRequest;
import com.dheerxpay.payment.proccessor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProcessorRouter {

    public Map<PaymentMethod, PaymentProcessor> paymentMethodProcessors;

    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        PaymentProcessor processor = paymentMethodProcessors.get(request.paymentMethod());
        if (processor == null) {
            throw new IllegalArgumentException("No payment processor found for method: " + request.paymentMethod());
        }
        return processor.charge(request);
    }


}
