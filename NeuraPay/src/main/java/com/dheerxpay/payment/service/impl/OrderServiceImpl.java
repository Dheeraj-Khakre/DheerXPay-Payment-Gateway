package com.dheerxpay.payment.service.impl;

import com.dheerxpay.common.enums.OrderStatus;
import com.dheerxpay.common.exceptions.BusinessRulesViolationException;
import com.dheerxpay.common.exceptions.DuplicateResourceException;
import com.dheerxpay.common.exceptions.ResourceNotFoundException;
import com.dheerxpay.payment.dto.request.OrderCreateRequest;
import com.dheerxpay.payment.dto.response.OrderResponse;
import com.dheerxpay.payment.dto.response.PaymentResponse;
import com.dheerxpay.payment.entities.OrderRecord;
import com.dheerxpay.payment.mapper.OrderMapper;
import com.dheerxpay.payment.repository.OrderRepository;
import com.dheerxpay.payment.repository.PaymentRepository;
import com.dheerxpay.payment.service.OrderService;
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
     private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse create(UUID merchantId, OrderCreateRequest request) {
        if(request.receipt()!=null && orderRepository.existsByMerchantIdAndReceipt(merchantId,request.receipt())){
            throw  new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE",request.receipt());
        }
        OrderRecord orderRecord = orderMapper.toEntity(request, merchantId);
        if (orderRecord.getExpiresAt() == null) {
            orderRecord.setExpiresAt(Instant.now().plusSeconds(30 * 60));
        }
        orderRecord = orderRepository.save(orderRecord);
        // TODO: public kafka event for order
        return orderMapper.toResponse(orderRecord);
    }

    @Override
    public OrderResponse gerById(UUID merchantId, UUID orderId) {
        OrderRecord orderRecord = orderRepository.findByIdAndMerchantId(orderId, merchantId).orElseThrow(() -> new ResourceNotFoundException("ORDER_RECORD", orderId));
        return orderMapper.toResponse(orderRecord);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID merchantId, UUID orderId) {
        OrderRecord orderRecord = orderRepository.findByIdAndMerchantId(orderId, merchantId).orElseThrow(() -> new ResourceNotFoundException("ORDER_RECORD", orderId));
        if(orderRecord.getStatus()== OrderStatus.CANCELLED || orderRecord.getStatus() == OrderStatus.PAID || orderRecord.getStatus()==OrderStatus.FAILED){
            throw  new BusinessRulesViolationException("ORDER_CANNOT_CANCEL","orcer cannot cancel this this error core "+orderRecord.getStatus().name());
        }
        orderRecord.setStatus(OrderStatus.CANCELLED);
        orderRecord  = orderRepository.save(orderRecord);


        return orderMapper.toResponse(orderRecord);
    }

    @Override
    public List<PaymentResponse> getListPayment(UUID merchantId, UUID orderId) {
        OrderRecord orderRecord = orderRepository.findByIdAndMerchantId(orderId, merchantId).orElseThrow(() -> new ResourceNotFoundException("ORDER_RECORD", orderId));
        return paymentRepository.findByOrder(orderRecord)
                .stream().map(orderMapper::toResponse).toList();


    }
}
