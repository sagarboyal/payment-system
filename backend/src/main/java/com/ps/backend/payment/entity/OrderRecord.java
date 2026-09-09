package com.ps.backend.payment.entity;

import com.ps.backend.common.entity.Money;
import com.ps.backend.common.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_orders_merchant_id", columnList = "merchant_id")
        }
)
public class OrderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    // Cross-boundary reference: payment module references merchant by UUID, avoiding tight ORM coupling
    @NotNull(message = "Merchant ID is required")
    @Column(name = "merchant_id", nullable = false, updatable = false)
    private UUID merchantId;

    // Merchant's internal reference ID (e.g., order_12345, cart_890)
    @Size(max = 40, message = "Receipt identifier cannot exceed 40 characters")
    @Column(name = "receipt", length = 40)
    private String receipt;

    @Valid
    @NotNull(message = "Order amount is required")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3))
    })
    private Money amount;

    @NotNull(message = "Order status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status = OrderStatus.CREATED;

    @Min(value = 0, message = "Attempts count cannot be negative")
    @Column(name = "attempts", nullable = false)
    private int attempts = 0;

    @Size(max = 15, message = "Notes cannot exceed 15 key-value pairs")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "notes", columnDefinition = "jsonb")
    private Map<String, Object> notes = new HashMap<>();

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}