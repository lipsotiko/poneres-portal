package com.poneres.portal.payments;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @GetMapping("/create-payment-intent")
    public PaymentIntentResponse createPaymentIntent() {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(2000L)
                        .setCurrency("usd")
                        .addPaymentMethodType("card")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                        )
                        .setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
                        .build();
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return PaymentIntentResponse.builder().clientSecret(paymentIntent.getClientSecret()).build();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
