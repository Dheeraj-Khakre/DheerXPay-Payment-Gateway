package com.dheerxpay.payment.config;

import com.dheerxpay.common.enums.PaymentMethod;
import com.dheerxpay.payment.gateway.PaymentAdapter;
import com.dheerxpay.payment.gateway.adapter.CardAdapter;
import com.dheerxpay.payment.gateway.adapter.UPIAdapter;
import com.dheerxpay.payment.gateway.adapter.WalletAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentAdapterConfig {

    @Bean
    public  Map<PaymentMethod, PaymentAdapter> paymentMethodAdapterMap() {

        return Map.of(
                PaymentMethod.CARD, new CardAdapter(),
                PaymentMethod.UPI, new UPIAdapter(),
                PaymentMethod.WALLET, new WalletAdapter(),
                PaymentMethod.NETBANKING, new com.dheerxpay.payment.gateway.adapter.NetBankingAdapter()
        );
    }
}
