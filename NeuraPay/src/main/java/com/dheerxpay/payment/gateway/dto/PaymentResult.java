package com.dheerxpay.payment.gateway.dto;

public  sealed  interface PaymentResult permits PaymentResult.Failure, PaymentResult.Pending, PaymentResult.Success
{
    public record Pending(String registerReference)implements PaymentResult {}
    public record Failure(String ErrorCode,String ErrorDescription) implements PaymentResult{}
    public record Success(String processorReference,String bankReference) implements PaymentResult{}
}
