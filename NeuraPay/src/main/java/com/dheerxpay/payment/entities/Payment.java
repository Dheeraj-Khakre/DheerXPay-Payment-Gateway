package com.dheerxpay.payment.entities;

import com.dheerxpay.common.entity.AuditableEntity;
import com.dheerxpay.common.entity.Money;
import com.dheerxpay.common.enums.PaymentMethod;
import com.dheerxpay.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(
        name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_payment_merchant_idempotency",
                        columnNames = {"merchant_id", "idempotency_key"}
                )
        },
        indexes = {
                @Index(name = "idx_payment_order_id", columnList = "order_id"),
                @Index(name = "idx_payment_merchant_id", columnList = "merchant_id"),
                @Index(name = "idx_payment_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_order"))
    private OrderRecord order;

    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Column(name = "idempotency_key", length = 255)
    private String idempotencyKey;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "amountUnit",
                    column = @Column(name = "amount_units", nullable = false)
            ),
            @AttributeOverride(
                    name = "currency",
                    column = @Column(name = "currency", nullable = false, length = 3)
            )
    })
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, length = 50)
    private PaymentMethod method;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "method_details", columnDefinition = "jsonb")
    private Map<String,Object> methodDetails;

    @Column(name = "bank_reference", length = 150)
    private String bankReference;

    @Column(name = "processor_reference", length = 150)
    private String processorReference;

    @Column(name = "error_code", length = 100)
    private String errorCode;

    @Column(name = "error_description", length = 1000)
    private String errorDescription;

    @Builder.Default
    @Column(name = "refunded_amount_paise", nullable = false)
    private long refundedAmountPaise = 0;

    @Column(name = "authorized_at")
    private Instant authorizedAt;

    @Column(name = "failed_at")
    private Instant failedAt;

    @Column(name = "captured_at")
    private Instant capturedAt;

    @Column(name = "settled_at")
    private Instant settledAt;

}
