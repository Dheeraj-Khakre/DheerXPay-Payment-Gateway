package com.dheerxpay.merchant.repository;

import com.dheerxpay.merchant.entities.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {


    List<ApiKey> findByMerchant_Id(UUID merchantId);
}
