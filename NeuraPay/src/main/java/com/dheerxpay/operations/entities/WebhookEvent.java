package com.dheerxpay.operations.entities;

import com.dheerxpay.common.entity.AuditableEntity;
import com.dheerxpay.common.enums.WebHookEventStatus;
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
        name = "webhook_events",
        indexes = {
                @Index(name = "idx_webhook_event_merchant_id", columnList = "merchant_id"),
                @Index(name = "idx_webhook_event_status_retry", columnList = "status, next_retry_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookEvent extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> payload;

    @Column(name = "target_url", nullable = false, length = 2048)
    private String targetUrl;

    @Column(name = "signature", nullable = false, length = 30)
    private String signature;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private WebHookEventStatus status;

    @Builder.Default
    @Column(name = "attempts", nullable = false)
    private Integer attempts = 0;

    @Column(name = "last_response_code")
    private Integer lastResponseCode;

    @Column(name = "last_response_body", columnDefinition = "jsonb")
    private String lastResponseBody;

    @Column(name = "next_retry_at")
    private Instant nextRetryAt;

    @Column(name = "last_retry_at")
    private Instant lastRetryAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;
}
