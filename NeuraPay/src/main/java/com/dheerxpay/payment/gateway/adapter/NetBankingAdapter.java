package com.dheerxpay.payment.gateway.adapter;

import com.dheerxpay.common.enums.PaymentMethod;
import com.dheerxpay.payment.gateway.PaymentAdapter;
import com.dheerxpay.payment.gateway.dto.PaymentRequest;
import com.dheerxpay.payment.gateway.dto.PaymentResult;
import com.dheerxpay.payment.proccessor.PaymentProcessor;
import com.dheerxpay.payment.proccessor.PaymentProcessorRouter;
import com.dheerxpay.payment.proccessor.dto.PaymentProcessorRequest;
import com.dheerxpay.payment.proccessor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor

public class NetBankingAdapter implements PaymentAdapter {

    private final PaymentProcessorRouter paymentProcessorRouter;
    @Override
    public PaymentResult initiatePayment(PaymentRequest paymentRequest) {
        // Implementation for initiating net banking payment
          log.info("Initiating net banking payment for paymentId: {}", paymentRequest.paymentId());
       try {


           PaymentProcessorRequest request = PaymentProcessorRequest.nonCard(
                   paymentRequest.paymentId(),
                   PaymentMethod.NETBANKING,
                   paymentRequest.amount(),
                   paymentRequest.methodDetails()
           );
           PaymentProcessorResponse processorResponse = paymentProcessorRouter.charge(request);


           return switch (processorResponse) {
               case PaymentProcessorResponse.Pending pending -> new PaymentResult.Pending(pending.processorReference());
               case PaymentProcessorResponse.Success success ->
                       new PaymentResult.Success(success.processorReference(), success.bankReference());
               case PaymentProcessorResponse.Failure failure ->
                       new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
           };
       }catch (Exception e) {
           log.error("Error initiating net banking payment for paymentId: {}. Error: {}", paymentRequest.paymentId(), e.getMessage());
           return new PaymentResult.Failure("NETBANKING_INITIATION_ERROR", "Failed to initiate net banking payment.");
       }
    }
}
