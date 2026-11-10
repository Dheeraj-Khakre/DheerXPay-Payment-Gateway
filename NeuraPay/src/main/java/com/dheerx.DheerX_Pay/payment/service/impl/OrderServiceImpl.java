package com.dheerx.DheerX_Pay.payment.service.impl;

import com.dheerx.DheerX_Pay.common.enums.OrderStatus;
import com.dheerx.DheerX_Pay.common.exceptions.BusinessRulesViolationException;
import com.dheerx.DheerX_Pay.common.exceptions.DuplicateResourceException;
import com.dheerx.DheerX_Pay.common.exceptions.ResourceNotFoundException;
import com.dheerx.DheerX_Pay.payment.dto.request.OrderCreateRequest;
import com.dheerx.DheerX_Pay.payment.dto.response.OrderResponse;
import com.dheerx.DheerX_Pay.payment.dto.response.PaymentResponse;
import com.dheerx.DheerX_Pay.payment.entities.OrderRecord;
import com.dheerx.DheerX_Pay.payment.repository.OrderRepository;
import com.dheerx.DheerX_Pay.payment.repository.PaymentRepository;
import com.dheerx.DheerX_Pay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;



@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

     private final OrderRepository orderRepository;
     private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public OrderResponse create(UUID merchantId, OrderCreateRequest request) {
        if(request.receipt()!=null && orderRepository.existsByMerchantIdAndReceipt(merchantId,request.receipt())){
            throw  new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE",request.receipt());
        }
        OrderRecord orderRecord = OrderRecord.builder()
                .amount(request.money())
                .attempts(1)
                .expiresAt(request.expireAt() != null ? request.expireAt() : Instant.now().plusSeconds(30 * 60))
                .merchantId(merchantId)
                .notes(request.notes())
                .receipt(request.receipt())
                .build();
        orderRecord = orderRepository.save(orderRecord);
        // TODO: public kafka event for order
        return OrderResponse.builder()
                .id(orderRecord.getId())
                .amount(orderRecord.getAmount())
                .attempts(orderRecord.getAttempts())
                .createAt(orderRecord.getCreatedAt())
                .expireAt(orderRecord.getExpiresAt())
                .merchantId(merchantId)
                .notes(orderRecord.getNotes())
                .receipt(orderRecord.getReceipt())
                .status(orderRecord.getStatus())
                .build();
    }

    @Override
    public OrderResponse gerById(UUID merchantId, UUID orderId) {
        OrderRecord orderRecord = orderRepository.findByIdAndMerchantId(merchantId, orderId).orElseThrow(() -> new ResourceNotFoundException("ORDER_RECORD", orderId));
        return OrderResponse.builder()
                .id(orderRecord.getId())
                .amount(orderRecord.getAmount())
                .attempts(orderRecord.getAttempts())
                .createAt(orderRecord.getCreatedAt())
                .expireAt(orderRecord.getExpiresAt())
                .merchantId(merchantId)
                .notes(orderRecord.getNotes())
                .receipt(orderRecord.getReceipt())
                .status(orderRecord.getStatus())
                .build();
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID merchantId, UUID orderId) {
        OrderRecord orderRecord = orderRepository.findByIdAndMerchantId(merchantId, orderId).orElseThrow(() -> new ResourceNotFoundException("ORDER_RECORD", orderId));
        if(orderRecord.getStatus()== OrderStatus.CANCELLED || orderRecord.getStatus() == OrderStatus.PAID || orderRecord.getStatus()==OrderStatus.FAILED){
            throw  new BusinessRulesViolationException("ORDER_CANNOT_CANCEL","orcer cannot cancel this this error core "+orderRecord.getStatus().name());
        }
        orderRecord.setStatus(OrderStatus.CANCELLED);
        orderRecord  = orderRepository.save(orderRecord);


        return OrderResponse.builder()
                .id(orderRecord.getId())
                .amount(orderRecord.getAmount())
                .attempts(orderRecord.getAttempts())
                .createAt(orderRecord.getCreatedAt())
                .expireAt(orderRecord.getExpiresAt())
                .merchantId(merchantId)
                .notes(orderRecord.getNotes())
                .receipt(orderRecord.getReceipt())
                .status(orderRecord.getStatus())
                .build();
    }

    @Override
    public List<PaymentResponse> getListPayment(UUID merchantId, UUID orderId) {
        OrderRecord orderRecord = orderRepository.findByIdAndMerchantId(merchantId, orderId).orElseThrow(() -> new ResourceNotFoundException("ORDER_RECORD", orderId));
        return paymentRepository.findByOrder(orderRecord)
                .stream().map(paymentRecord -> PaymentResponse.builder()
                        .id(paymentRecord.getId())
                        .amount(paymentRecord.getAmount())
                        .capturedAt(paymentRecord.getCapturedAt())
                        .createdAt(paymentRecord.getCreatedAt())
                        .updatedAt(paymentRecord.getUpdatedAt())
                        .authorizedAt(paymentRecord.getAuthorizedAt())
                        .failedAt(paymentRecord.getFailedAt())
                        .bankReference(paymentRecord.getBankReference())
                        .errorCode(paymentRecord.getErrorCode())
                        .errorDescription(paymentRecord.getErrorDescription())
                        .merchantId(paymentRecord.getMerchantId())
                        .orderId(paymentRecord.getOrder().getId())
                        .method(paymentRecord.getMethod())
                        .status(paymentRecord.getStatus())
                        .build()).toList();


    }
}
