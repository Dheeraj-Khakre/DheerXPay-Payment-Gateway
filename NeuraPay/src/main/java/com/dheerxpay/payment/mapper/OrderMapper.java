package com.dheerxpay.payment.mapper;

import com.dheerxpay.payment.dto.request.OrderCreateRequest;
import com.dheerxpay.payment.dto.response.OrderResponse;
import com.dheerxpay.payment.dto.response.PaymentResponse;
import com.dheerxpay.payment.entities.OrderRecord;
import com.dheerxpay.payment.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "merchantId", source = "merchantId")
    @Mapping(target = "amount", source = "request.money")
    @Mapping(target = "expiresAt", source = "request.expireAt")
    @Mapping(target = "attempts", constant = "1")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "idempotencyKey", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    OrderRecord toEntity(OrderCreateRequest request, UUID merchantId);

    @Mapping(target = "expireAt", source = "expiresAt")
    @Mapping(target = "createAt", source = "createdAt")
    OrderResponse toResponse(OrderRecord order);

    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toResponse(Payment payment);
}
