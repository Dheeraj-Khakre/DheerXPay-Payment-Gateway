package com.dheerx.DheerX_Pay.common.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Money {
    private int amountUnits;

    private String currency;

    protected Money() {
    }

    private Money(int amountUnit, String currency) {
        this.amountUnits = amountUnit;
        this.currency = currency;
    }

    public static Money of(int amountUnits, String currency) {
        if (currency == null || !currency.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("Currency must be a 3-letter uppercase ISO 4217 code");
        }
        return new Money(amountUnits, currency);
    }

    public static Money inr(int amountUnits) {
        return new Money(amountUnits, "INR");
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add Money with different currencies");
        }
        return new Money(Math.addExact(this.amountUnits, other.amountUnits), this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract Money with different currencies");
        }
        return new Money(Math.subtractExact(this.amountUnits, other.amountUnits), this.currency);
    }
}
