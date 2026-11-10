package com.dheerx.DheerX_Pay.payment.mapper;

import com.dheerx.DheerX_Pay.payment.dto.request.OrderCreateRequest;
import com.dheerx.DheerX_Pay.payment.dto.response.OrderResponse;
import com.dheerx.DheerX_Pay.payment.dto.response.PaymentResponse;
import com.dheerx.DheerX_Pay.payment.entities.OrderRecord;
import com.dheerx.DheerX_Pay.payment.entities.Payment;
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
