package com.ps.backend.payment.service.impl;

import com.ps.backend.common.enums.OrderStatus;
import com.ps.backend.common.enums.PaymentStatus;
import com.ps.backend.common.exception.BusinessRuleViolateException;
import com.ps.backend.common.exception.ResourceNotFoundException;
import com.ps.backend.payment.dto.request.PaymentInitRequest;
import com.ps.backend.payment.dto.response.PaymentResponse;
import com.ps.backend.payment.entity.OrderRecord;
import com.ps.backend.payment.entity.Payment;
import com.ps.backend.payment.gateway.PaymentGatewayRouter;
import com.ps.backend.payment.gateway.dto.PaymentRequest;
import com.ps.backend.payment.gateway.dto.PaymentResult;
import com.ps.backend.payment.mapper.PaymentMapper;
import com.ps.backend.payment.repository.OrderRepository;
import com.ps.backend.payment.repository.PaymentRepository;
import com.ps.backend.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest request) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(request.orderId(), merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.orderId()));

        if(order.getOrderStatus() != OrderStatus.CREATED && order.getOrderStatus() != OrderStatus.ATTEMPTED){
            throw new BusinessRuleViolateException("ORDER_NOT_PAYABLE",
                    "Order cannot accept payment in status: "+order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts()+1);

        Payment payment = Payment.builder()
                .order(order)
                .merchantId(merchantId)
                .amount(order.getAmount())
                .status(PaymentStatus.CREATED)
                .method(request.method())
                .methodDetails(request.methodDetails())
                .build();

        payment = paymentRepository.save(payment);

        PaymentRequest paymentRequest = new PaymentRequest(payment.getId(),
                request.orderId(),
                merchantId,
                order.getAmount(),
                request.method(),
                request.methodDetails()
                );
        PaymentResult result = paymentGatewayRouter.initiate(paymentRequest);

        switch (result) {
            case PaymentResult.Pending pending -> payment.setProcessorReference(pending.registrationRef());
            case PaymentResult.Failure failure -> {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setErrorCode(failure.errorCode());
                payment.setErrorDescription(failure.errorDescription());
            }
        }

        payment = paymentRepository.save(payment);
        orderRepository.save(order);

        return paymentMapper.toResponse(payment);
    }
}
