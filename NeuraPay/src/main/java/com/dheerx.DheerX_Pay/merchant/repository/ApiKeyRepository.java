package com.dheerx.DheerX_Pay.merchant.repository;

import com.dheerx.DheerX_Pay.merchant.entities.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {


    List<ApiKey> findByMerchant_Id(UUID merchantId);
}
