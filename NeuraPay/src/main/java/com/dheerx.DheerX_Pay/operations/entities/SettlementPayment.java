package com.dheerx.DheerX_Pay.operations.entities;

import com.dheerx.DheerX_Pay.common.entity.AuditableEntity;
import com.dheerx.DheerX_Pay.payment.entities.Payment;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "settlement_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementPayment extends AuditableEntity {

    @EmbeddedId
    private SettlementPaymentId id;

    @MapsId("settlementId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "settlement_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_settlement_payment_settlement")
    )
    private Settlement settlement;

}
