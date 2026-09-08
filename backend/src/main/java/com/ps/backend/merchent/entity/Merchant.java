package com.ps.backend.merchent.entity;

import com.ps.backend.common.enums.BusinessType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "merchants", indexes = {
        @Index(name = "idx_merchant_email", columnList = "email"),
        @Index(name = "idx_merchant_pan", columnList = "pan_id")
})
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Merchant contact name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number (must be 10 digits starting with 6-9)")
    @Column(name = "contact_number", nullable = false, length = 15)
    private String contactNumber;

    @NotNull(message = "Business type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", nullable = false, length = 50)
    private BusinessType businessType;

    @NotBlank(message = "Business/Legal name is required")
    @Size(min = 2, max = 200, message = "Business name must be between 2 and 200 characters")
    @Column(name = "business_name", nullable = false, length = 200)
    private String businessName;

    @URL(message = "Invalid website or application URL format")
    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    @NotNull(message = "Account status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status = Status.PENDING_KYC;

    @Pattern(
            regexp = "^$|^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
            message = "Invalid GSTIN format"
    )
    @Column(name = "gst_id", length = 15)
    private String gstId;

   @NotBlank(message = "PAN is mandatory for merchant onboarding")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Invalid Indian PAN format")
    @Column(name = "pan_id", nullable = false, length = 10)
    private String panId;

    @NotBlank(message = "Settlement account number is required")
    @Pattern(regexp = "^[0-9]{9,18}$", message = "Bank account number must be between 9 and 18 digits")
    @Column(name = "settlement_bank_account", nullable = false, length = 20)
    private String settlementBankAccount;

    @NotBlank(message = "IFSC code is required")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code format (e.g., HDFC0000123)")
    @Column(name = "settlement_bank_ifsc", nullable = false, length = 11)
    private String settlementBankIfsc;

    @NotBlank(message = "Account holder name is required")
    @Size(min = 2, max = 150, message = "Account holder name must be between 2 and 150 characters")
    @Column(name = "settlement_bank_account_holder_name", nullable = false, length = 150)
    private String settlementBankAccountHolderName;
}