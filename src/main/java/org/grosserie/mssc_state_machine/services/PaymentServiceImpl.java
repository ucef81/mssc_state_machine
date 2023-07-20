package org.grosserie.mssc_state_machine.services;

import lombok.RequiredArgsConstructor;
import org.grosserie.mssc_state_machine.domain.Payment;
import org.grosserie.mssc_state_machine.domain.PaymentEvent;
import org.grosserie.mssc_state_machine.domain.PaymentState;
import org.grosserie.mssc_state_machine.repository.PaymentRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;
    public static final String PAYMENT_ID_HEADER = "payment_id";

    @Override
    public Payment NewPayment(Payment p) {
        p.setState(PaymentState.NEW);
        return paymentRepository.save(p);
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, PaymentEvent.PRE_AUTHORIZE);

        return null;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) {

        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, PaymentEvent.AUTH_APPROVED);
        return null;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) {

        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, PaymentEvent.AUTH_DECLINED);
        return null;
    }

    private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> sm, PaymentEvent event){
        Message msg = MessageBuilder.withPayload(event)
                .setHeader(PAYMENT_ID_HEADER, paymentId)
                .build();
        sm.sendEvent(msg);
        }
    private StateMachine<PaymentState, PaymentEvent> build(Long paymentId){
        Payment payment = paymentRepository.findById(paymentId).get();

        StateMachine<PaymentState, PaymentEvent> sm = stateMachineFactory.getStateMachine(Long.toString(payment.getId()));
        sm.stop();
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.resetStateMachine(new DefaultStateMachineContext<>(payment.getState(),null,null,null));
                });
        sm.start();
        return sm;
    }
}
