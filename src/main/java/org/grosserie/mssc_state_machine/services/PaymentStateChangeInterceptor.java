package org.grosserie.mssc_state_machine.services;


import lombok.RequiredArgsConstructor;
import org.grosserie.mssc_state_machine.domain.Payment;
import org.grosserie.mssc_state_machine.domain.PaymentEvent;
import org.grosserie.mssc_state_machine.domain.PaymentState;
import org.grosserie.mssc_state_machine.repository.PaymentRepository;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

    private final PaymentRepository paymentRepository;

    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message, Transition<PaymentState, PaymentEvent> transition, StateMachine<PaymentState, PaymentEvent> stateMachine, StateMachine<PaymentState, PaymentEvent> rootStateMachine) {

        Optional.ofNullable(Long.class.cast(message.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID_HEADER, -1L)))
                .ifPresent(
                        paymentId -> {
                            Payment payment = paymentRepository.findById(paymentId).get();
                            payment.setState(state.getId());
                            paymentRepository.save(payment);
                        });


    }
}
