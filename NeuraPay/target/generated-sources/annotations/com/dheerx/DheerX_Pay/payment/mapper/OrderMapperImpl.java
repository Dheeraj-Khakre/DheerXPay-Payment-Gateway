package com.dheerx.DheerX_Pay.payment.mapper;

import com.dheerx.DheerX_Pay.payment.dto.request.OrderCreateRequest;
import com.dheerx.DheerX_Pay.payment.dto.response.OrderResponse;
import com.dheerx.DheerX_Pay.payment.dto.response.PaymentResponse;
import com.dheerx.DheerX_Pay.payment.entities.OrderRecord;
import com.dheerx.DheerX_Pay.payment.entities.Payment;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-10T22:27:16+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.5.0.2 (Microsoft)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderRecord toEntity(OrderCreateRequest request, UUID merchantId) {
        if ( request == null && merchantId == null ) {
            return null;
        }

        OrderRecord.OrderRecordBuilder orderRecord = OrderRecord.builder();

        if ( request != null ) {
            orderRecord.amount( request.money() );
            orderRecord.expiresAt( request.expireAt() );
            orderRecord.receipt( request.receipt() );
            Map<String, Object> map = request.notes();
            if ( map != null ) {
                orderRecord.notes( new LinkedHashMap<String, Object>( map ) );
            }
        }
        orderRecord.merchantId( merchantId );
        orderRecord.attempts( 1 );

        return orderRecord.build();
    }

    @Override
    public OrderResponse toResponse(OrderRecord order) {
        if ( order == null ) {
            return null;
        }

        OrderResponse.OrderResponseBuilder orderResponse = OrderResponse.builder();

        orderResponse.expireAt( order.getExpiresAt() );
        orderResponse.createAt( order.getCreatedAt() );
        orderResponse.id( order.getId() );
        orderResponse.merchantId( order.getMerchantId() );
        orderResponse.status( order.getStatus() );
        orderResponse.amount( order.getAmount() );
        orderResponse.attempts( order.getAttempts() );
        orderResponse.receipt( order.getReceipt() );
        Map<String, Object> map = order.getNotes();
        if ( map != null ) {
            orderResponse.notes( new LinkedHashMap<String, Object>( map ) );
        }

        return orderResponse.build();
    }

    @Override
    public PaymentResponse toResponse(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentResponse.PaymentResponseBuilder paymentResponse = PaymentResponse.builder();

        paymentResponse.orderId( paymentOrderId( payment ) );
        paymentResponse.id( payment.getId() );
        paymentResponse.merchantId( payment.getMerchantId() );
        paymentResponse.idempotencyKey( payment.getIdempotencyKey() );
        paymentResponse.amount( payment.getAmount() );
        paymentResponse.status( payment.getStatus() );
        paymentResponse.method( payment.getMethod() );
        Map<String, Object> map = payment.getMethodDetails();
        if ( map != null ) {
            paymentResponse.methodDetails( new LinkedHashMap<String, Object>( map ) );
        }
        paymentResponse.bankReference( payment.getBankReference() );
        paymentResponse.errorCode( payment.getErrorCode() );
        paymentResponse.errorDescription( payment.getErrorDescription() );
        paymentResponse.refundedAmountPaise( payment.getRefundedAmountPaise() );
        paymentResponse.authorizedAt( payment.getAuthorizedAt() );
        paymentResponse.failedAt( payment.getFailedAt() );
        paymentResponse.capturedAt( payment.getCapturedAt() );
        paymentResponse.settledAt( payment.getSettledAt() );
        paymentResponse.createdAt( payment.getCreatedAt() );
        paymentResponse.updatedAt( payment.getUpdatedAt() );

        return paymentResponse.build();
    }

    private UUID paymentOrderId(Payment payment) {
        OrderRecord order = payment.getOrder();
        if ( order == null ) {
            return null;
        }
        return order.getId();
    }
}
