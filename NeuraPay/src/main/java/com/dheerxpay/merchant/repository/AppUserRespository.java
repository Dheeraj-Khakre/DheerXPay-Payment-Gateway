package com.dheerxpay.merchant.repository;

import com.dheerxpay.merchant.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppUserRespository  extends JpaRepository<AppUser, UUID> {

}
