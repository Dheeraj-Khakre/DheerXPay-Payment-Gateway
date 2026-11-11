package com.dheerxpay.vault.entities;

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
        name = "card_tokens",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_card_token_token", columnNames = "token")
        },
        indexes = {
                @Index(name = "idx_card_token_vault_card_id", columnList = "vault_card_id"),
                @Index(name = "idx_card_token_customer_id", columnList = "customer_id"),
                @Index(name = "idx_card_token_merchant_id", columnList = "merchant_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardToken extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "token", nullable = false, unique = true, length = 255)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vault_card_id", nullable = false, foreignKey = @ForeignKey(name = "fk_card_token_vault_card"))
    private VaultCard vaultCard;


    @Column(name = "customer_id", nullable = false)
    private UUID customer;

    @Column(name = "merchant_id", nullable = false)
    private UUID merchant;

    private Instant revokedAt;
}
