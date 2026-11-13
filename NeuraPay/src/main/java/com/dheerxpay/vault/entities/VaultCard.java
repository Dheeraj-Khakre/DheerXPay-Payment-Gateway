package com.dheerxpay.vault.entities;

import com.dheerxpay.common.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "vault_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaultCard extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "encrypted_pan", nullable = false, columnDefinition = "bytea")
    private byte[] encryptedPan;

    @Column(name = "encrypted_dek", nullable = false, columnDefinition = "bytea")
    private byte[] encryptedDek;

    @Column(name = "last_four", nullable = false, length = 4)
    private String lastFour;

    @Column(name = "brand", length = 30)
    private String brand;

    @Column(name = "bin", length = 6 )
    private String bin;

    @Column(name = "card_holder_name", length = 100)
    private String cardHolderName;

    @Column(name = "expiry_month", nullable = false)
    private Integer expiryMonth;

    @Column(name = "expiry_year", nullable = false)
    private Integer expiryYear;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
