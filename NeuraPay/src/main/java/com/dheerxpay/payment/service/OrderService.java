package com.dheerxpay.payment.service;

import com.dheerxpay.payment.dto.request.OrderCreateRequest;
import com.dheerxpay.payment.dto.response.OrderResponse;
import com.dheerxpay.payment.dto.response.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse create(UUID merchantId, OrderCreateRequest request);

    OrderResponse gerById(UUID merchantId,UUID orderId);

    OrderResponse cancelOrder(UUID merchantId, UUID orderId);

    List<PaymentResponse> getListPayment(UUID merchantId, UUID orderId);

}
