package com.ps.backend.merchant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_customer_merchant_email",
                        columnNames = {"merchant_id", "email"}
                )
        },
        indexes = {
                @Index(name = "idx_customer_merchant_contact", columnList = "merchant_id, contact_number")
        }
)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "Associated merchant is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Size(max = 100, message = "Customer name cannot exceed 100 characters")
    @Column(name = "name", length = 100)
    private String name;

    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    @Column(name = "email", length = 150)
    private String email;

    @Pattern(
            regexp = "^$|^(\\+?[1-9]\\d{1,14}|[6-9]\\d{9})$",
            message = "Invalid phone number (must be a 10-digit Indian mobile or E.164 international format)"
    )
    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}