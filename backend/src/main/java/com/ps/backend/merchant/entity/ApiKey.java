package com.ps.backend.merchant.entity;

import com.ps.backend.common.enums.Environment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "api_keys",
        indexes = {
                @Index(name = "idx_api_keys_merchant_id", columnList = "merchant_id")
        }
)
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "Associated merchant is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    // Public key / client identifier (e.g., rzp_test_xxx, rzp_live_xxx)
    // unique = true automatically generates a B-Tree index for API auth lookups
    @NotBlank(message = "Key ID is required")
    @Size(max = 64, message = "Key ID cannot exceed 64 characters")
    @Column(name = "key_id", nullable = false, unique = true, length = 64)
    private String keyId;

    // Never store plain-text secrets in a payment gateway.
    // Store a one-way cryptographic hash (e.g., Argon2id or SHA-256 with salt).
    @NotBlank(message = "Secret key hash is required")
    @Size(max = 255, message = "Secret key hash cannot exceed 255 characters")
    @Column(name = "secret_key_hash", nullable = false, length = 255)
    private String secretKey;

    @NotNull(message = "Environment is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "environment", nullable = false, length = 20)
    private Environment environment;

    @Column(name = "is_enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "rotated_at")
    private LocalDateTime rotatedAt;

    // Dual-key rotation support: allows the retired key to process payments until expiry
    @Column(name = "grace_period_expiry_at")
    private LocalDateTime gracePeriodExpiryAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}