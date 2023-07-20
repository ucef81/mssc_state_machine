package org.grosserie.mssc_state_machine.repository;

import org.grosserie.mssc_state_machine.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentRepository extends JpaRepository<Payment, Long> , JpaSpecificationExecutor<Payment> {
}
