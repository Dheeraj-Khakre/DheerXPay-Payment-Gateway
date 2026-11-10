package com.dheerx.DheerX_Pay.operations.entities;

import com.dheerx.DheerX_Pay.common.entity.AuditableEntity;
import com.dheerx.DheerX_Pay.common.entity.Money;
import com.dheerx.DheerX_Pay.common.enums.RefoundStatus;
import com.dheerx.DheerX_Pay.payment.entities.Payment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(
        name = "refunds",
        indexes = {
                @Index(name = "idx_refund_payment_id", columnList = "payment_id"),
                @Index(name = "idx_refund_merchant_id", columnList = "merchant_id"),
                @Index(name = "idx_refund_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_refund_payment"))
    private Payment payment;

    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "amount", nullable = false)
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private RefoundStatus status = RefoundStatus.PENDING;

    @Column(name = "bank_reference", length = 150)
    private String bankReference;

    @Column(name = "error_code", length = 100)
    private String errorCode;

    @Column(name = "error_description", length = 1000,columnDefinition = "jsonb")
   @JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    private Map<String ,Object>  errorDescription;

    @Column(name = "processed_at")
    private Instant processedAt;

}
