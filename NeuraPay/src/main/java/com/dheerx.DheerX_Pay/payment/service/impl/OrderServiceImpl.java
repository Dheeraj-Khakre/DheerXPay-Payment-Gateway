package com.dheerx.DheerX_Pay.payment.service.impl;

import com.dheerx.DheerX_Pay.common.exceptions.DuplicateResourceException;
import com.dheerx.DheerX_Pay.payment.dto.request.OrderCreateRequest;
import com.dheerx.DheerX_Pay.payment.dto.response.CreateOrderResponse;
import com.dheerx.DheerX_Pay.payment.entities.OrderRecord;
import com.dheerx.DheerX_Pay.payment.repository.OrderRepository;
import com.dheerx.DheerX_Pay.payment.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

     private final OrderRepository orderRepository;

    @Override
    @Transactional
    public CreateOrderResponse create(UUID merchantId, OrderCreateRequest request) {
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
        return CreateOrderResponse.builder()
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
}
