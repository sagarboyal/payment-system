package com.ps.backend.payment.config;

import com.ps.backend.common.enums.PaymentMethod;
import com.ps.backend.payment.gateway.PaymentAdapter;
import com.ps.backend.payment.gateway.adapter.CardPaymentAdapter;
import com.ps.backend.payment.gateway.adapter.NetBankingAdapter;
import com.ps.backend.payment.gateway.adapter.UpiPaymentAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentAdapterConfig {

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap(){
        return Map.of(
               PaymentMethod.CARD, new CardPaymentAdapter(),
               PaymentMethod.NET_BANKING, new NetBankingAdapter(),
               PaymentMethod.UPI, new UpiPaymentAdapter()
        );
    }
}
