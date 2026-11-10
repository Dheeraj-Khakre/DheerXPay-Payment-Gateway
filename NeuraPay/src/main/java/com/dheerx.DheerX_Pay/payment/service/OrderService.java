package com.dheerx.DheerX_Pay.payment.service;

import com.dheerx.DheerX_Pay.payment.dto.request.OrderCreateRequest;
import com.dheerx.DheerX_Pay.payment.dto.response.CreateOrderResponse;

import java.util.UUID;

public interface OrderService {
    CreateOrderResponse create(UUID merchantId, OrderCreateRequest request);
}
