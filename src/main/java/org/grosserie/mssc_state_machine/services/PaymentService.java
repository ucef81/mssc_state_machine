package org.grosserie.mssc_state_machine.services;

import org.grosserie.mssc_state_machine.domain.Payment;
import org.grosserie.mssc_state_machine.domain.PaymentEvent;
import org.grosserie.mssc_state_machine.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {
    Payment newPayment(Payment p);

    StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);

    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);

    StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);


}
