package com.ps.backend.payment.service.impl;

import com.ps.backend.common.enums.OrderStatus;
import com.ps.backend.common.exception.BusinessRuleViolateException;
import com.ps.backend.common.exception.DuplicateResourceException;
import com.ps.backend.common.exception.ResourceNotFoundException;
import com.ps.backend.payment.dto.request.CreateOrderRequest;
import com.ps.backend.payment.dto.response.OrderResponse;
import com.ps.backend.payment.dto.response.PaymentResponse;
import com.ps.backend.payment.entity.OrderRecord;
import com.ps.backend.payment.entity.Payment;
import com.ps.backend.payment.mapper.OrderMapper;
import com.ps.backend.payment.mapper.PaymentMapper;
import com.ps.backend.payment.repository.OrderRepository;
import com.ps.backend.payment.repository.PaymentRepository;
import com.ps.backend.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private  final PaymentMapper paymentMapper;
    private final OrderMapper  orderMapper;

    @Value("${payment.order.default-order-expiry-minutes : 30}")
    private int defaultExpiryMinutes;

    @Override
    @Transactional
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {
        if(request.receipt() != null && orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE",
                    "Order with receipt already exists: "+request.receipt());
        }

        OrderRecord order = OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())
                .merchantId(merchantId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() : LocalDateTime.now().plusMinutes(defaultExpiryMinutes))
                .build();

               order = orderRepository.save(order);

               // TODO: Publish Kafka event about order creation

        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord order =  orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancel(UUID merchantId, UUID orderId) {
        OrderRecord order =  orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        if(order.getOrderStatus() == OrderStatus.CANCELED || order.getOrderStatus() == OrderStatus.PAID){
            throw new BusinessRuleViolateException("ORDER_CANNOT_CANCEL",
                    "Cannot cancel order with status: "+order.getOrderStatus().name());
        }

        order.setOrderStatus(OrderStatus.CANCELED);
       order = orderRepository.save(order);
       return orderMapper.toResponse(order);
    }
    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {
        OrderRecord order =  orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        List<Payment> paymentList = paymentRepository.findByOrder_Id(order);

//        return paymentList.stream().map(
//                paymentMapper::toResponse
//        ).collect(Collectors.toList());

        return paymentMapper.toResponseList(paymentList);
    }

}
