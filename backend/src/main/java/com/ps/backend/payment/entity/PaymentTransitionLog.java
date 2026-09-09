package com.ps.backend.payment.entity;

import com.ps.backend.common.enums.PaymentEvent;
import com.ps.backend.common.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "payment_transition_logs",
        indexes = {
                @Index(name = "idx_pay_transitions_payment_id", columnList = "payment_id")
        }
)
public class PaymentTransitionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "Associated payment is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false, updatable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 30, updatable = false)
    private PaymentStatus fromStatus;

    @NotNull(message = "Target status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 30, updatable = false)
    private PaymentStatus toStatus;

    @NotNull(message = "Payment event trigger is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "event", nullable = false, length = 50, updatable = false)
    private PaymentEvent event;

    // Identifies the initiator (e.g., "SYSTEM_WORKER", "WEBHOOK:BANK_GATEWAY", "MERCHANT_API")
    @NotBlank(message = "Actor is required")
    @Size(max = 100, message = "Actor identifier cannot exceed 100 characters")
    @Column(name = "actor", nullable = false, length = 100, updatable = false)
    private String actor;

    // Append-only audit record: timestamp is fixed at creation
    @CreationTimestamp
    @Column(name = "occurred_at", nullable = false, updatable = false)
    private LocalDateTime occurredAt;

    // Convenient constructor for state machine listeners
    public PaymentTransitionLog(Payment payment, PaymentStatus fromStatus, PaymentStatus toStatus, PaymentEvent event, String actor) {
        this.payment = payment;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.event = event;
        this.actor = actor;
    }
}