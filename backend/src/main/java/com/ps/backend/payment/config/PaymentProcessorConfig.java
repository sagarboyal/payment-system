package com.ps.backend.payment.config;

import com.ps.backend.common.enums.PaymentMethod;
import com.ps.backend.payment.processor.PaymentProcessor;
import com.ps.backend.payment.processor.strategy.CardPaymentProcessor;
import com.ps.backend.payment.processor.strategy.NetBankingPaymentProcessor;
import com.ps.backend.payment.processor.strategy.UpiPaymentProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentProcessorConfig {

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap() {
        return Map.of(
                PaymentMethod.CARD, new CardPaymentProcessor(),
                PaymentMethod.NET_BANKING, new NetBankingPaymentProcessor(),
                PaymentMethod.UPI, new UpiPaymentProcessor()
        );
    }
}
