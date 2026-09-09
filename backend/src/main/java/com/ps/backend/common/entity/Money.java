package com.ps.backend.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Required by Hibernate reflection
public class Money implements Comparable<Money>, Serializable {

    // Smallest currency unit (paisa for INR, cents for USD)
    @Column(name = "amount", nullable = false)
    private Long amountUnits;

    // ISO 4217 3-letter currency code (e.g., INR, USD, EUR)
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    private Money(Long amountUnits, String currency) {
        if (amountUnits == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null || currency.trim().length() != 3) {
            throw new IllegalArgumentException("Valid 3-letter ISO-4217 currency code is required");
        }
        this.amountUnits = amountUnits;
        this.currency = currency.trim().toUpperCase();
    }

    // --- Static Factory Methods ---

    public static Money of(Long amountUnits, String currency) {
        return new Money(amountUnits, currency);
    }

    public static Money ofPaisa(Long paisa) {
        return new Money(paisa, "INR");
    }

    public static Money zero(String currency) {
        return new Money(0L, currency);
    }

    // Helper: converts major currency (e.g., ₹100.50) into subunits (10050 paisa)
    public static Money fromMajorUnits(BigDecimal amount, String currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        Currency cur = Currency.getInstance(currency.toUpperCase());
        int fractionDigits = cur.getDefaultFractionDigits();
        long units = amount.setScale(fractionDigits, RoundingMode.UNNECESSARY)
                .movePointRight(fractionDigits)
                .longValueExact();
        return new Money(units, currency);
    }

    // --- Domain Operations ---

    public Money add(Money other) {
        assertSameCurrency(other);
        return new Money(this.amountUnits + other.amountUnits, this.currency);
    }

    public Money subtract(Money other) {
        assertSameCurrency(other);
        return new Money(this.amountUnits - other.amountUnits, this.currency);
    }

    public Money multiply(long factor) {
        return new Money(this.amountUnits * factor, this.currency);
    }

    public boolean isGreaterThan(Money other) {
        return compareTo(other) > 0;
    }

    public boolean isLessThan(Money other) {
        return compareTo(other) < 0;
    }

    public boolean isZero() {
        return this.amountUnits == 0L;
    }

    public boolean isPositive() {
        return this.amountUnits > 0L;
    }

    // Converts back to human-readable BigDecimal for receipts / API display (10050 -> 100.50)
    public BigDecimal toMajorUnits() {
        int fractionDigits = Currency.getInstance(this.currency).getDefaultFractionDigits();
        return BigDecimal.valueOf(this.amountUnits).movePointLeft(fractionDigits);
    }

    private void assertSameCurrency(Money other) {
        if (other == null || !this.currency.equalsIgnoreCase(other.currency)) {
            throw new IllegalArgumentException(
                    "Currency mismatch: cannot perform operation between " + this.currency + " and " + (other != null ? other.currency : "null")
            );
        }
    }

    // --- Value Object Equality & Comparisons ---

    @Override
    public int compareTo(Money other) {
        assertSameCurrency(other);
        return Long.compare(this.amountUnits, other.amountUnits);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return Objects.equals(amountUnits, money.amountUnits) &&
                Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountUnits, currency);
    }

    @Override
    public String toString() {
        return currency + " " + toMajorUnits().toPlainString();
    }
}