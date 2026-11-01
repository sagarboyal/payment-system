package com.ps.backend.payment.repository;

import com.ps.backend.payment.dto.response.OrderResponse;
import com.ps.backend.payment.entity.OrderRecord;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {

    Optional<OrderRecord> findByIdAndMerchantId(UUID orderId, UUID merchantId);

    boolean existsByMerchantIdAndReceipt(UUID merchantId, @Size(max = 100) String receipt);
}
