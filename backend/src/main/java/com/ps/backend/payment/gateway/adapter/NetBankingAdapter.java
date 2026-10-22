package com.ps.backend.payment.gateway.adapter;

import com.ps.backend.payment.gateway.PaymentAdapter;
import com.ps.backend.payment.gateway.dto.PaymentRequest;
import com.ps.backend.payment.gateway.dto.PaymentResult;

public class NetBankingAdapter implements PaymentAdapter {

    @Override
    public PaymentResult initiate(PaymentRequest request){

        return null;

    }
}
