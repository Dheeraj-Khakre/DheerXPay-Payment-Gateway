package com.dheerxpay.payment.repository;

import com.dheerxpay.payment.entities.OrderRecord;
import com.dheerxpay.payment.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment , UUID> {
        Collection<Payment> findByOrder(OrderRecord orderRecord);
}
