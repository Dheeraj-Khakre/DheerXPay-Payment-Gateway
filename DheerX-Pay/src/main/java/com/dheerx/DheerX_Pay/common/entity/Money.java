package com.dheerx.DheerX_Pay.common.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Money {
    private int amountUnit;

    private String currency;

    protected Money() {
    }

    private Money(int amountUnit, String currency) {
        this.amountUnit = amountUnit;
        this.currency = currency;
    }

    public static Money of(int amountUnit, String currency) {
        if (currency == null || !currency.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("Currency must be a 3-letter uppercase ISO 4217 code");
        }
        return new Money(amountUnit, currency);
    }

    public static Money inr(int amountUnit) {
        return new Money(amountUnit, "INR");
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add Money with different currencies");
        }
        return new Money(Math.addExact(this.amountUnit, other.amountUnit), this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract Money with different currencies");
        }
        return new Money(Math.subtractExact(this.amountUnit, other.amountUnit), this.currency);
    }
}
