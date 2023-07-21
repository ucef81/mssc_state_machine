package org.grosserie.mssc_state_machine.services;

import org.grosserie.mssc_state_machine.domain.Payment;
import org.grosserie.mssc_state_machine.domain.PaymentEvent;
import org.grosserie.mssc_state_machine.domain.PaymentState;
import org.grosserie.mssc_state_machine.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    Payment payment;

    @BeforeEach
    void setUp() {
        payment =  Payment.builder().amount(new BigDecimal(10.2)).build();

    }

    @Transactional
    @Test
    void preAuth(){
        Payment savedPayment = paymentService.newPayment(payment);

        System.out.println("Should be NEW");
        System.out.println(savedPayment.getState());
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
        Payment preAuthPayment = paymentRepository.findById(payment.getId()).get();
        System.out.println("Should be PRE_AUTH or PRE_AUTH_ERROR");
        System.out.println(sm.getState().getId());

        System.out.println(preAuthPayment);

    }
}