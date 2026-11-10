package com.dheerx.DheerX_Pay.merchant.repository;

import com.dheerx.DheerX_Pay.merchant.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppUserRespository  extends JpaRepository<AppUser, UUID> {

}
