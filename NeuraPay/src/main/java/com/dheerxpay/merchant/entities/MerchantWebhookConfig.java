package com.dheerxpay.merchant.entities;

import com.dheerxpay.common.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "merchant_webhook_config",
        indexes = {
                @Index(name = "idx_webhook_config_merchant_id", columnList = "merchant_id")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantWebhookConfig extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "merchant_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_webhook_config_merchant")
    )
    private Merchant merchant;

    @Column(name = "target_url", nullable = false, length = 2048)
    private String targetUrl;

    @Column(name = "event_type_filter", length = 1000)
    private String eventTypeFilter;

    @Builder.Default
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;


    @Column(name = "event_type", length = 100)
    private String eventType;

    @Column(name = "webhook_secret_hash", nullable = false, length = 255)
    private String webhookSecretHash;

}
