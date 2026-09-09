package com.ps.backend.payment.entity;

import com.ps.backend.common.entity.Money;
import com.ps.backend.common.enums.PaymentMethod;
import com.ps.backend.common.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "payments",
        uniqueConstraints = {
                // Prevents duplicate charges if the client retries an API call with the same idempotency key
                @UniqueConstraint(
                        name = "uk_payments_merchant_idempotency",
                        columnNames = {"merchant_id", "idempotency_key"}
                )
        },
        indexes = {
                // Required: PostgreSQL does not auto-index FKs; speeds up fetching all payment attempts for an order
                @Index(name = "idx_payments_order_id", columnList = "order_id")
        }
)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "Associated order is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private OrderRecord order;

    @NotNull(message = "Merchant ID is required")
    @Column(name = "merchant_id", nullable = false, updatable = false)
    private UUID merchantId;

    @Valid
    @NotNull(message = "Payment amount is required")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "amount", nullable = false, updatable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3, updatable = false))
    })
    private Money amount;

    // Client-supplied key to prevent accidental double charges on network dropouts
    @Size(max = 100, message = "Idempotency key cannot exceed 100 characters")
    @Column(name = "idempotency_key", length = 100)
    private String idempotencyKey;

    @NotNull(message = "Payment status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PaymentStatus status = PaymentStatus.PENDING;

    @NotNull(message = "Payment method is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, length = 30)
    private PaymentMethod method;

    // Stores non-PCI sanitized metadata (e.g. {"vpa": "user@okaxis"} or {"card_network": "VISA", "last4": "4242"})
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "method_details", columnDefinition = "jsonb")
    private Map<String, Object> methodDetails;

    // Bank-issued RRN (Retrieval Reference Number), UTR, or Acquirer Reference Number
    @Size(max = 100, message = "Bank reference cannot exceed 100 characters")
    @Column(name = "bank_reference", length = 100)
    private String bankReference;

    @Size(max = 50, message = "Error code cannot exceed 50 characters")
    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Size(max = 500, message = "Error message cannot exceed 500 characters")
    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "authorized_at")
    private LocalDateTime authorizedAt;

    @Column(name = "captured_at")
    private LocalDateTime capturedAt;

    @Column(name = "refunded_at")
    private LocalDateTime refundedAt;

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    @Column(name = "failed_at")
    private LocalDateTime failedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}