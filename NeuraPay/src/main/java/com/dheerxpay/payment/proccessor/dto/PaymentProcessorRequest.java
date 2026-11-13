package com.dheerxpay.payment.proccessor.dto;

import com.dheerxpay.common.entity.Money;
import com.dheerxpay.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentProcessorRequest(
        UUID paymentId,
        UUID processingId,
        PaymentMethod paymentMethod,
        Money amount,
        Map<String, Object> methodDetails,
        String pan,
        String expiry
) {

   public static PaymentProcessorRequest card(UUID PaymentId,Money money,Map<String,Object> methodDetails,String pan,String expiry) {
        return new PaymentProcessorRequest(
                PaymentId,
                UUID.randomUUID(),
                PaymentMethod.CARD,
                money,
                methodDetails,
                pan,
                expiry
        );
    }

   public static PaymentProcessorRequest nonCard(UUID PaymentId,PaymentMethod method,Money money,Map<String,Object> methodDetails) {
            return new PaymentProcessorRequest(
                    PaymentId,
                    UUID.randomUUID(),
                    method,
                    money,
                    methodDetails,
                    null,
                    null
            );

    }

}
