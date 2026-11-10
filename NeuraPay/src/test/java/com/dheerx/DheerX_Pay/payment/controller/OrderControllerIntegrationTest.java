package com.dheerx.DheerX_Pay.payment.controller;

import com.dheerx.DheerX_Pay.common.entity.Money;
import com.dheerx.DheerX_Pay.common.enums.OrderStatus;
import com.dheerx.DheerX_Pay.common.exceptions.ResourceNotFoundException;
import com.dheerx.DheerX_Pay.payment.dto.request.OrderCreateRequest;
import com.dheerx.DheerX_Pay.payment.dto.response.OrderResponse;
import com.dheerx.DheerX_Pay.payment.dto.response.PaymentResponse;
import com.dheerx.DheerX_Pay.payment.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerIntegrationTest {

    private static final UUID CONTROLLER_MERCHANT_ID =
            UUID.fromString("9c64a369-81ea-472f-8c79-721fed3526d2");

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private OrderService orderService;

    @Test
    void createOrderReturnsCreatedResponse() throws Exception {
        OrderResponse response = OrderResponse.builder()
                .id(UUID.randomUUID())
                .amount(Money.inr(2500))
                .status(OrderStatus.CREATED)
                .build();
        when(orderService.create(eq(CONTROLLER_MERCHANT_ID), any(OrderCreateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/order")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "money": {"amountUnits": 2500, "currency": "INR"},
                                  "receipt": "receipt-1",
                                  "notes": {"source": "integration-test"},
                                  "expireAt": "2030-01-01T00:00:00Z"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CREATED"))
                .andExpect(jsonPath("$.data.amount.amountUnits").value(2500));

        verify(orderService).create(eq(CONTROLLER_MERCHANT_ID), any(OrderCreateRequest.class));
    }

    @Test
    void createOrderRejectsMissingMoneyBeforeCallingService() throws Exception {
        mockMvc.perform(post("/api/v1/order")
                        .contentType(APPLICATION_JSON)
                        .content("{\"receipt\":\"receipt-1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void getOrderReturnsOrderResponse() throws Exception {
        UUID orderId = UUID.randomUUID();
        when(orderService.gerById(CONTROLLER_MERCHANT_ID, orderId))
                .thenReturn(OrderResponse.builder().id(orderId).status(OrderStatus.CREATED).build());

        mockMvc.perform(get("/api/v1/order/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(orderId.toString()))
                .andExpect(jsonPath("$.data.status").value("CREATED"));

        verify(orderService).gerById(CONTROLLER_MERCHANT_ID, orderId);
    }

    @Test
    void missingOrderReturnsNotFound() throws Exception {
        UUID orderId = UUID.randomUUID();
        when(orderService.gerById(CONTROLLER_MERCHANT_ID, orderId))
                .thenThrow(new ResourceNotFoundException("ORDER_RECORD", orderId));

        mockMvc.perform(get("/api/v1/order/{orderId}", orderId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCore").value("ORDER_RECORD_NOT_FOUND"));
    }

    @Test
    void cancelOrderReturnsUpdatedOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        when(orderService.cancelOrder(CONTROLLER_MERCHANT_ID, orderId))
                .thenReturn(OrderResponse.builder().id(orderId).status(OrderStatus.CANCELLED).build());

        mockMvc.perform(delete("/api/v1/order/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CANCELLED"));

        verify(orderService).cancelOrder(CONTROLLER_MERCHANT_ID, orderId);
    }

    @Test
    void listPaymentsReturnsPaymentResponses() throws Exception {
        UUID orderId = UUID.randomUUID();
        when(orderService.getListPayment(CONTROLLER_MERCHANT_ID, orderId))
                .thenReturn(List.of(new PaymentResponse()));

        mockMvc.perform(get("/api/v1/order/{orderId}/payments", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1));

        verify(orderService).getListPayment(CONTROLLER_MERCHANT_ID, orderId);
    }
}
