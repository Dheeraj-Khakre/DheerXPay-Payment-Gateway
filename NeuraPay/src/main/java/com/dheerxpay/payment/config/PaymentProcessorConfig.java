package com.dheerxpay.payment.config;

import com.dheerxpay.common.enums.PaymentMethod;
import com.dheerxpay.payment.proccessor.PaymentProcessor;
import com.dheerxpay.payment.proccessor.strategy.CardPaymentProcessor;
import com.dheerxpay.payment.proccessor.strategy.NetBankingPaymentProcessor;
import com.dheerxpay.payment.proccessor.strategy.UpiPaymentProcessor;
import com.dheerxpay.payment.proccessor.strategy.WallatPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentProcessorConfig {

    private final CardPaymentProcessor cardPaymentProcessor;
    private final UpiPaymentProcessor upiPaymentProcessor;
    private final WallatPaymentProcessor walletPaymentProcessor;
    private final NetBankingPaymentProcessor netBankingPaymentProcessor;

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentMethodProcessorMap() {
        return Map.of(
                PaymentMethod.CARD, cardPaymentProcessor,
                PaymentMethod.UPI, upiPaymentProcessor,
                PaymentMethod.WALLET, walletPaymentProcessor,
                PaymentMethod.NETBANKING, netBankingPaymentProcessor
        );
    }
}
