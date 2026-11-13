package com.dheerxpay.payment.service.impl;

import com.dheerxpay.common.enums.OrderStatus;
import com.dheerxpay.common.enums.PaymentStatus;
import com.dheerxpay.common.exceptions.BusinessRulesViolationException;
import com.dheerxpay.common.exceptions.ResourceNotFoundException;
import com.dheerxpay.payment.dto.request.PaymentInitRequest;
import com.dheerxpay.payment.dto.response.PaymentResponse;
import com.dheerxpay.payment.entities.OrderRecord;
import com.dheerxpay.payment.entities.Payment;
import com.dheerxpay.payment.gateway.PaymentGatewayRouter;
import com.dheerxpay.payment.gateway.dto.PaymentRequest;
import com.dheerxpay.payment.gateway.dto.PaymentResult;
import com.dheerxpay.payment.mapper.OrderMapper;
import com.dheerxpay.payment.repository.OrderRepository;
import com.dheerxpay.payment.repository.PaymentRepository;
import com.dheerxpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final OrderMapper orderMapper;
    @Override
    @Transactional
    public PaymentResponse initiatePayment(UUID merchantId, PaymentInitRequest request) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(request.orderId(), merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("ODER_NOT_FOUND", "Order not found for the given orderId and merchantId"+ request.orderId() + " " + merchantId));

        if(order.getStatus() != null && order.getStatus().name().equals("PAID")) {
            throw new BusinessRulesViolationException("ORDER_NOT_PAYABLE", "Payment has already been made for this order.");
        }
        if(order.getStatus() != null && order.getStatus().name().equals("CANCELLED")) {
            throw new BusinessRulesViolationException("ORDER_CANCELLED", "Payment cannot be initiated for a cancelled order.");
        }
        if(order.getStatus()!= OrderStatus.CREATED && order.getStatus()!= OrderStatus.COMPLETED) {
            throw new BusinessRulesViolationException("ORDER_NOT_PAYABLE", "Payment cannot be initiated for this order status: " + order.getStatus());
        }
        order.setStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts() + 1);
        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getAmount())
                .merchantId(merchantId)
                .method(request.method())
                .methodDetails(request.methodDetails())
                .status(PaymentStatus.CREATED)
                .build();

       payment= paymentRepository.save(payment);
     ;

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .merchantId(payment.getMerchantId())
                .methodDetails(payment.getMethodDetails())
                .amount(payment.getAmount())
                .paymentMethod(payment.getMethod())
                .orderId(order.getId())
                .build();
        PaymentResult paymentResult = paymentGatewayRouter.initiatePayment(paymentRequest);

        switch (paymentResult){
            case PaymentResult.Pending pending -> {
                payment.setStatus(PaymentStatus.PENDING);
                payment.setProcessorReference(pending.registerReference());
            }
            case PaymentResult.Failure failure -> {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setErrorCode(failure.ErrorCode());
                payment.setErrorDescription(failure.ErrorDescription());

                //throw new BusinessRulesViolationException(failure.ErrorCode(), failure.ErrorDescription());
            }
            case PaymentResult.Success success -> {
                payment.setStatus(PaymentStatus.AUTHORIZED);
                payment.setProcessorReference(success.processorReference());
                payment.setBankReference(success.bankReference());
            }
        }
        payment = paymentRepository.save(payment);
        order= orderRepository.save(order);

        return orderMapper.toResponse(payment);

    }
}
