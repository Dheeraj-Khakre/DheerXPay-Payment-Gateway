package com.dheerx.DheerX_Pay.merchant.entities;

import com.dheerx.DheerX_Pay.common.entity.AuditableEntity;
import com.dheerx.DheerX_Pay.common.enums.Environment;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "api_keys",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_api_key_key_id", columnNames = "key_id")
        },
        indexes = {
                @Index(name = "idx_api_key_merchant_id", columnList = "merchant_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKey extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "merchant_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_api_key_merchant")
    )
    private Merchant merchant;

    @Column(name = "key_id", nullable = false, length = 100,unique = true)
    private String keyId;

    @Column(name = "key_secret_hash", nullable = false,unique = true, length = 255)
    private String keySecretHash;

    @Column(name = "webhook_secret_hash", length = 255)
    private String webhookSecretHash;

    @Column(name = "environment", nullable = false, length = 30)
    private Environment environment;

    @Builder.Default
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "last_used_at")
    private Instant lastUsedAt;


    @Column(name = "rotated_at")
    private Instant rotatedAt;

    @Column(name = "grace_period_expires_at")
    private Instant gracePeriodExpiresAt;
}
