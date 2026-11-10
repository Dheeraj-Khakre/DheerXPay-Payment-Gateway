package com.dheerx.DheerX_Pay.payment.repository;

import com.dheerx.DheerX_Pay.payment.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment , UUID> {
}
