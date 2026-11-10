package com.dheerx.DheerX_Pay.operations.entities;

import com.dheerx.DheerX_Pay.common.entity.AuditableEntity;
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
        name = "dlq_events",
        indexes = {
                @Index(name = "idx_dlq_event_webhook_event_id", columnList = "webhook_event_id"),
                @Index(name = "idx_dlq_event_merchant_id", columnList = "merchant_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DlqEvent extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "webhook_event_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dlq_event_webhook_event"))
    private WebhookEvent webhookEvent;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> payload;

    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Column(name = "final_error", nullable = false, length = 2000)
    private String finalError;

    @Column(name = "moved_at", nullable = false, updatable = false)
    private Instant movedAt;

    @Column(name = "replayed_at")
    private Instant replayedAt;

}
