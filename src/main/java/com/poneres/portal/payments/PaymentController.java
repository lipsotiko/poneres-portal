package com.poneres.portal.payments;

import com.poneres.portal.security.auth0.AuthenticationService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PaymentIntentRepository paymentIntentRepository;

    @GetMapping("/create-payment-intent")
    public PaymentIntentResponse createPaymentIntent() {
        String email = authenticationService.getEmail();
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(2000L)
                        .setCurrency("usd")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                        )
                        .setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
                        .build();
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            paymentIntentRepository.save(PaymentIntentRequested.builder()
                    .paymentIntentId(paymentIntent.getId())
                    .email(email)
                    .createdAt(LocalDateTime.now())
                    .build());

            return PaymentIntentResponse.builder().clientSecret(paymentIntent.getClientSecret()).build();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/create-payment-intent/webhook")
    public ResponseEntity<String> createPaymentIntentWebhook(
            HttpServletRequest request,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        String payload;

        try {
            payload = new BufferedReader(request.getReader())
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            return ResponseEntity.of(Optional.of("Failed to read payload"));
        }

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, "whsec_b27a0d4fcd1a1388dafb837dd392556ff0f7a7355827160a99283199e82a3620");
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

        PaymentIntent intent = (PaymentIntent) event
                .getDataObjectDeserializer()
                .getObject()
                .orElse(null);

        if (intent == null) {
            return ResponseEntity.of(Optional.of("Unable to deserialize object"));
        }

        PaymentIntentRequested byPaymentIntentId = paymentIntentRepository.findByPaymentIntentId(intent.getId());

        switch (event.getType()) {

            case "payment_intent.succeeded":
                System.out.println("Succeeded: " + intent.getId());
                byPaymentIntentId.setStatus("succeeded");
                paymentIntentRepository.save(byPaymentIntentId);
                // TODO: Inform the user that the payment succeeded...
                break;

            case "payment_intent.payment_failed":
                System.out.println("Failed: " + intent.getId());
                byPaymentIntentId.setStatus("payment_failed");
                paymentIntentRepository.save(byPaymentIntentId);
                // TODO: Inform the user that the payment failed...
                break;

            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return ResponseEntity.ok().build();
    }
}
