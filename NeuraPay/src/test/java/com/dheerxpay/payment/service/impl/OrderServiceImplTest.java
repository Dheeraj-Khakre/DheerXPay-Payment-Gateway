package com.dheerxpay.payment.service.impl;

import com.dheerxpay.common.entity.Money;
import com.dheerxpay.common.enums.OrderStatus;
import com.dheerxpay.common.exceptions.BusinessRulesViolationException;
import com.dheerxpay.common.exceptions.DuplicateResourceException;
import com.dheerxpay.common.exceptions.ResourceNotFoundException;
import com.dheerxpay.payment.dto.request.OrderCreateRequest;
import com.dheerxpay.payment.dto.response.OrderResponse;
import com.dheerxpay.payment.dto.response.PaymentResponse;
import com.dheerxpay.payment.entities.OrderRecord;
import com.dheerxpay.payment.entities.Payment;
import com.dheerxpay.payment.mapper.OrderMapper;
import com.dheerxpay.payment.repository.OrderRepository;
import com.dheerxpay.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createMapsPersistsAndReturnsOrder() {
        UUID merchantId = UUID.randomUUID();
        OrderCreateRequest request = OrderCreateRequest.builder().money(Money.inr(2500)).build();
        OrderRecord order = OrderRecord.builder().attempts(1).build();
        OrderResponse response = OrderResponse.builder().build();
        when(orderMapper.toEntity(request, merchantId)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(response);

        var beforeCreate = java.time.Instant.now();
        assertSame(response, orderService.create(merchantId, request));

        assertEquals(1, order.getAttempts());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertFalse(order.getExpiresAt().isBefore(beforeCreate.plusSeconds(30 * 60)));
        assertFalse(order.getExpiresAt().isAfter(java.time.Instant.now().plusSeconds(30 * 60)));
        verify(orderRepository).save(order);
    }

    @Test
    void createUsesRequestedExpiryWhenProvided() {
        UUID merchantId = UUID.randomUUID();
        var expiry = java.time.Instant.parse("2030-01-01T00:00:00Z");
        OrderCreateRequest request = OrderCreateRequest.builder().money(Money.inr(100)).expireAt(expiry).build();
        OrderRecord order = OrderRecord.builder().expiresAt(expiry).build();
        when(orderMapper.toEntity(request, merchantId)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(OrderResponse.builder().build());

        orderService.create(merchantId, request);

        assertEquals(expiry, order.getExpiresAt());
    }

    @Test
    void createRejectsDuplicateReceipt() {
        UUID merchantId = UUID.randomUUID();
        OrderCreateRequest request = OrderCreateRequest.builder().receipt("receipt-1").build();
        when(orderRepository.existsByMerchantIdAndReceipt(merchantId, "receipt-1")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> orderService.create(merchantId, request));

        verify(orderMapper, never()).toEntity(request, merchantId);
        verify(orderRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void getByIdMapsExistingOrder() {
        UUID merchantId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        OrderRecord order = new OrderRecord();
        OrderResponse response = OrderResponse.builder().build();
        when(orderRepository.findByIdAndMerchantId(merchantId, orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(response);

        assertSame(response, orderService.gerById(merchantId, orderId));
    }

    @Test
    void getByIdRejectsMissingOrder() {
        UUID merchantId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findByIdAndMerchantId(merchantId, orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.gerById(merchantId, orderId));
    }

    @Test
    void cancelUpdatesOrderAndMapsResponse() {
        UUID merchantId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        OrderRecord order = OrderRecord.builder().status(OrderStatus.CREATED).build();
        OrderResponse response = OrderResponse.builder().status(OrderStatus.CANCELLED).build();
        when(orderRepository.findByIdAndMerchantId(merchantId, orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(response);

        assertSame(response, orderService.cancelOrder(merchantId, orderId));
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void cancelRejectsPaidOrder() {
        UUID merchantId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        OrderRecord order = OrderRecord.builder().status(OrderStatus.PAID).build();
        when(orderRepository.findByIdAndMerchantId(merchantId, orderId)).thenReturn(Optional.of(order));

        assertThrows(BusinessRulesViolationException.class, () -> orderService.cancelOrder(merchantId, orderId));

        verify(orderRepository, never()).save(order);
    }

    @Test
    void listsMappedPaymentsForOrder() {
        UUID merchantId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        OrderRecord order = new OrderRecord();
        Payment payment = new Payment();
        PaymentResponse response = new PaymentResponse();
        when(orderRepository.findByIdAndMerchantId(merchantId, orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrder(order)).thenReturn(List.of(payment));
        when(orderMapper.toResponse(payment)).thenReturn(response);

        assertSame(response, orderService.getListPayment(merchantId, orderId).getFirst());
    }
}
