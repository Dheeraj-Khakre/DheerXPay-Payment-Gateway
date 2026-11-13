package com.dheerxpay.merchant.entities;

import com.dheerxpay.common.enums.BusinessType;
import com.dheerxpay.common.enums.MerchantStatus;
import com.dheerxpay.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "merchants",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_merchant_code", columnNames = "merchant_code"),
                @UniqueConstraint(name = "uk_merchant_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_merchant_phone", columnNames = "phone")
        },
        indexes = {
                @Index(name = "idx_merchant_status", columnList = "status"),
                @Index(name = "idx_merchant_business_name", columnList = "business_name"),
                @Index(name = "idx_merchant_created_at", columnList = "created_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Merchant extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "merchant_code", nullable = false, unique = true, length = 30)
    private String merchantCode;

    @Column(name = "business_name", nullable = false, length = 150)
    private String businessName;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", nullable = false, length = 30)
    private BusinessType businessType;

    @Column(name = "legal_name", nullable = false, length = 150)
    private String legalName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    @Column(name = "website_url", length = 255)
    private String websiteUrl;

    @Column(name = "gst_id", length = 20)
    private String gstId;

    @Column(name = "pan_id", length = 10)
    private String pan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MerchantStatus status = MerchantStatus.PENDING_KYC;

    @Column(name = "kyc_verified", nullable = false)
    private boolean kycVerified = false;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "addressLine1", column = @Column(name = "address_line_1")),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "address_line_2")),
            @AttributeOverride(name = "landmark", column = @Column(name = "landmark")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "state", column = @Column(name = "state")),
            @AttributeOverride(name = "country", column = @Column(name = "country")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "postal_code"))
    })
    private MerchantAddress address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "accountHolderName", column = @Column(name = "account_holder_name")),
            @AttributeOverride(name = "bankName", column = @Column(name = "bank_name")),
            @AttributeOverride(name = "accountNumber", column = @Column(name = "account_number")),
            @AttributeOverride(name = "ifscCode", column = @Column(name = "ifsc_code")),
            @AttributeOverride(name = "branchName", column = @Column(name = "branch_name")),
            @AttributeOverride(name = "upiId", column = @Column(name = "upi_id")),
            @AttributeOverride(name = "beneficiaryType", column = @Column(name = "beneficiary_type"))
    })
    private MerchantBankDetails bankDetails;


    @Version
    @Column(name = "version", nullable = false)
    private Long version;


}
