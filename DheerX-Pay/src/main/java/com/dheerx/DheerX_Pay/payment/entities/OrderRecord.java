package com.dheerx.DheerX_Pay.payment.entities;

import com.dheerx.DheerX_Pay.common.entity.Money;
import com.dheerx.DheerX_Pay.common.enums.OrderStatus;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
        name = "order_records",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_order_record_merchant_idempotency",
                        columnNames = {"merchant_id", "idempotency_key"}
                )
        },
        indexes = {
                @Index(name = "idx_order_record_merchant_id", columnList = "merchant_id"),
                @Index(name = "idx_order_record_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Column(name = "idempotency_key", nullable = false, length = 255)
    private String idempotencyKey;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "amountUnit",
                    column = @Column(name = "amount_paise", nullable = false)
            ),
            @AttributeOverride(
                    name = "currency",
                    column = @Column(name = "currency", nullable = false, length = 3)
            )
    })
    private Money amount;

    @Column(name = "status", nullable = false, length = 30)
    private OrderStatus status = OrderStatus.CREATED;

    @Builder.Default
    @Column(name = "attempts", nullable = false)
    private Integer attempts = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "notes", columnDefinition = "jsonb")
    private Map<String,Object> notes;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;
}
