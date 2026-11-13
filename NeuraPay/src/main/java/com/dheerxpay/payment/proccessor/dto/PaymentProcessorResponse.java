package com.dheerxpay.payment.proccessor.dto;

public sealed interface PaymentProcessorResponse
        permits
        PaymentProcessorResponse.Success,
        PaymentProcessorResponse.Failure,
        PaymentProcessorResponse.Pending {

    record Pending(String processorReference) implements PaymentProcessorResponse {
    }

    record Success(String processorReference, String bankReference) implements PaymentProcessorResponse {
    }

    record Failure(String errorCode, String errorDescription) implements PaymentProcessorResponse {
    }
}