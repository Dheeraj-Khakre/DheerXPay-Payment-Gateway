package com.dheerxpay.payment.config;

import com.dheerxpay.common.enums.PaymentMethod;
import com.dheerxpay.payment.gateway.PaymentAdapter;
import com.dheerxpay.payment.gateway.adapter.CardAdapter;
import com.dheerxpay.payment.gateway.adapter.NetBankingAdapter;
import com.dheerxpay.payment.gateway.adapter.UPIAdapter;
import com.dheerxpay.payment.gateway.adapter.WalletAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentAdapterConfig {

    private final CardAdapter cardAdapter;
    private final UPIAdapter upiAdapter;
    private final WalletAdapter walletAdapter;
    private final NetBankingAdapter netBankingAdapter;

    @Bean
    public  Map<PaymentMethod, PaymentAdapter> paymentMethodAdapterMap() {

        return Map.of(
                PaymentMethod.CARD, cardAdapter,
                PaymentMethod.UPI, upiAdapter,
                PaymentMethod.WALLET, walletAdapter,
                PaymentMethod.NETBANKING, netBankingAdapter
        );
    }
}
