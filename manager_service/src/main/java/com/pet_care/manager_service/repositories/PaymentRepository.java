package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
