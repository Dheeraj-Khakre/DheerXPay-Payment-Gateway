package com.dheerxpay.merchant.repository;

import com.dheerxpay.merchant.entities.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {

    boolean existsByMerchantCode(String merchantCode);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Merchant> findByMerchantCode(String merchantCode);
}

